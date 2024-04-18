package com.rocky.whisper.data.home.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.rocky.whisper.data.Constants.COLLECTION_ROOMS
import com.rocky.whisper.data.Constants.FIELD_USERS
import com.rocky.whisper.data.home.Chatroom
import com.rocky.whisper.data.home.local.ChatroomDao
import com.rocky.whisper.data.home.toExternal
import com.rocky.whisper.data.home.toLocal
import com.rocky.whisper.data.user.User
import com.rocky.whisper.data.user.repository.DefaultUserRepository
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

class DefaultChatroomRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val chatroomDao: ChatroomDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : ChatroomRepository {

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
                            db.collection(DefaultUserRepository.COLLECTION_USERS).document(userId)
                                .get().await()
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

    override suspend fun updateFirstVisibleIndex(roomId: String, index: Int) {
        chatroomDao.updateFirstVisibleIndex(roomId, index)
    }
}