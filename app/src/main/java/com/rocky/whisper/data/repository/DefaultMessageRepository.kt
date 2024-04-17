package com.rocky.whisper.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.rocky.whisper.data.Chatroom
import com.rocky.whisper.data.Message
import com.rocky.whisper.data.User
import com.rocky.whisper.data.repository.DefaultUserRepository.Companion.COLLECTION_USERS
import com.rocky.whisper.data.source.local.ChatroomDao
import com.rocky.whisper.data.source.local.MessageDao
import com.rocky.whisper.data.toExternal
import com.rocky.whisper.data.toLocal
import com.rocky.whisper.di.ApplicationScope
import com.rocky.whisper.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class DefaultMessageRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val chatroomDao: ChatroomDao,
    private val messageDao: MessageDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : MessageRepository {
    companion object {
        const val COLLECTION_ROOMS = "rooms"
        const val COLLECTION_MESSAGES = "messages"
        const val FIELD_USERS = "users"
        const val FIELD_LAST_MESSAGE = "lastMessage"
        const val FIELD_LAST_UPDATE = "lastUpdate"
    }

    override fun createRoom(id: String) {
        scope.launch(dispatcher) {
            try {
                val userDetails = mutableListOf<User>()
                val userIds = mutableListOf<String>().apply {
                    auth.currentUser?.uid?.let { add(it) }
                    add(id)
                }
                val room = db.collection(COLLECTION_ROOMS).document()

                val userDetailDeferreds = userIds.map { userId ->
                    async {
                        val userDetail =
                            db.collection(COLLECTION_USERS).document(userId).get().await()
                        userDetail.toObject(User::class.java)
                    }
                }

                userDetailDeferreds.forEach { deferred ->
                    deferred.await()?.let { userDetails.add(it) }
                }

                val chatroom = Chatroom(
                    id = room.id,
                    users = userIds,
                    userDetails = userDetails,
                    lastMessage = "",
                    lastUpdate = System.currentTimeMillis()
                )
                room.set(chatroom).await()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun fetchChatroom() {
        db.collection(COLLECTION_ROOMS)
            .whereArrayContains(FIELD_USERS, auth.currentUser?.uid ?: "")
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    Timber.e(error)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    handleChatroomDocumentChanges(it)
                }
            }
    }

    private fun handleChatroomDocumentChanges(snapshot: QuerySnapshot) {
        scope.launch(dispatcher) {
            for (dc in snapshot.documentChanges) {
                val chatroom = dc.document.toObject(Chatroom::class.java).toLocal()
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        val room = chatroomDao.getChatroom(chatroom.id)
                        if (room == null) chatroomDao.insertAll(chatroom)
                        else chatroomDao.updateWithoutFirstVisibleIndex(
                            chatroom.id,
                            chatroom.userDetails,
                            chatroom.lastMessage,
                            chatroom.lastUpdate
                        )
                    }

                    DocumentChange.Type.MODIFIED -> {
                        chatroomDao.update(chatroom)
                    }

                    DocumentChange.Type.REMOVED -> {
                        chatroomDao.delete(chatroom)
                    }
                }
            }
        }
    }

    override suspend fun observeChatroom(): Flow<List<Chatroom>> {
        return chatroomDao.observeAll()
            .map { it.map { localChatroom -> localChatroom.toExternal() } }
    }

    override suspend fun sendMessage(roomId: String, message: String) {
        try {
            val user = auth.currentUser
            user?.uid?.let {
                val doc = db.collection(COLLECTION_ROOMS)
                    .document(roomId)
                    .collection(COLLECTION_MESSAGES).document()
                doc.set(Message(doc.id, it, message, System.currentTimeMillis())).await()
                updateLastMessage(roomId, message)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun fetchMessage(roomId: String): ListenerRegistration {
        return db.collection(COLLECTION_ROOMS)
            .document(roomId)
            .collection(COLLECTION_MESSAGES)
            .orderBy(FIELD_LAST_UPDATE)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    Timber.e(error)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    scope.launch(dispatcher) {
                        handleMessageDocumentChanges(it, roomId)
                    }
                }
            }
    }

    private fun handleMessageDocumentChanges(snapshot: QuerySnapshot, roomId: String) {
        scope.launch {
            for (dc in snapshot.documentChanges) {
                val message = dc.document.toObject(Message::class.java).toLocal(roomId)
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        messageDao.insertAll(message)
                    }

                    DocumentChange.Type.MODIFIED -> {
                        messageDao.update(message.id, message.message)
                    }

                    DocumentChange.Type.REMOVED -> {
                        messageDao.delete(message)
                    }
                }
            }
        }
    }

    private suspend fun updateLastMessage(roomId: String, message: String) {
        try {
            val updates = hashMapOf(
                FIELD_LAST_MESSAGE to message,
                FIELD_LAST_UPDATE to System.currentTimeMillis(),
            )

            db.collection((COLLECTION_ROOMS))
                .document(roomId)
                .update(updates as Map<String, Any>).await()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun observeMessage(roomId: String): Flow<List<Message>> {
        return messageDao.observeAll(roomId)
            .map { it.map { localMessage -> localMessage.toExternal() } }
    }

    override suspend fun observeMessageCount(): Flow<Int> {
        return messageDao.observeMessageCount()
    }

    override suspend fun updateFirstVisibleIndex(roomId: String, index: Int) {
        chatroomDao.updateFirstVisibleIndex(roomId, index)
    }
}