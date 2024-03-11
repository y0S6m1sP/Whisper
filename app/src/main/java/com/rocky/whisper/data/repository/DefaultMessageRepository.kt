package com.rocky.whisper.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
                    val rooms = it.toObjects(Chatroom::class.java).map { room -> room.toLocal() }
                    scope.launch(dispatcher) {
                        // convert chatroom to vararg
                        chatroomDao.insertAll(*rooms.toTypedArray())
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
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun fetchMessage(roomId: String) {
        db.collection(COLLECTION_ROOMS)
            .document(roomId)
            .collection(COLLECTION_MESSAGES)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    Timber.e(error)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    scope.launch(dispatcher) {
                        val messages = it.toObjects(Message::class.java)
                            .map { message -> message.toLocal(roomId) }
                            .sortedByDescending { message -> message.lastUpdate }
                        if (messages.isNotEmpty()) {
                            messageDao.insertAll(*messages.toTypedArray())
                            chatroomDao.updateLastMessage(
                                roomId,
                                messages.first().message,
                                System.currentTimeMillis()
                            )
                        }
                    }
                }
            }
    }

    override suspend fun observeMessage(roomId: String): Flow<List<Message>> {
        return messageDao.observeAll(roomId)
            .map { it.map { localMessage -> localMessage.toExternal() } }
    }
}