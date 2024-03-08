package com.rocky.whisper.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rocky.whisper.data.Message
import com.rocky.whisper.data.Room
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class DefaultMessageRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : MessageRepository {
    companion object {
        const val COLLECTION_ROOMS = "rooms"
        const val COLLECTION_MESSAGES = "messages"
        const val FIELD_PARTICIPANTS = "participants"
    }

    override suspend fun createRoom(id: String) {
        try {
            val user =auth.currentUser
            val participants = mutableListOf<String>()
            user?.uid?.let { participants.add(it) }
            participants.add(id)
            db.collection(COLLECTION_ROOMS)
                .add(Room(participants, System.currentTimeMillis()))
                .await()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun fetchRoom(): Flow<List<String>> {
        return callbackFlow {
            val user = auth.currentUser
            val listener = db.collection(COLLECTION_ROOMS)
                .whereArrayContains(FIELD_PARTICIPANTS, user?.uid ?: "")
                .addSnapshotListener { snapshot, error ->
                    error?.let {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    val roomIds = mutableListOf<String>()
                    if (snapshot != null) {
                        for (document in snapshot) {
                            roomIds.add(document.id)
                        }
                    }
                    trySend(roomIds)
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    override suspend fun sendMessage(roomId: String, message: String) {
        try {
            val user = auth.currentUser
            user?.uid?.let {
                db.collection(COLLECTION_ROOMS)
                    .document(roomId)
                    .collection(COLLECTION_MESSAGES)
                    .add(Message(it, message, System.currentTimeMillis())).await()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun fetchMessage(roomId: String): Flow<List<Message>> {
        return callbackFlow {
            delay(300)
            val listener = db.collection(COLLECTION_ROOMS)
                .document(roomId)
                .collection(COLLECTION_MESSAGES)
                .addSnapshotListener { snapshot, error ->
                    error?.let {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    snapshot?.let {
                        trySend(it.toObjects(Message::class.java))
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }
}