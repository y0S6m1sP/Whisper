package com.rocky.whisper.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rocky.whisper.data.Message
import com.rocky.whisper.data.Room
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class DefaultMessageRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val signInRepository: SignInRepository
) : MessageRepository {
    companion object {
        const val COLLECTION_ROOMS = "rooms"
        const val FIELD_PARTICIPANTS = "participants"
    }

    override suspend fun createRoom(id: String) {
        try {
            val user = signInRepository.getOrSignInAnonymously()
            val participants = mutableListOf<String>()
            user?.uid?.let { participants.add(it) }
            participants.add(id)
            db.collection(COLLECTION_ROOMS).add(Room(participants, System.currentTimeMillis()))
                .await()
        } catch (e: Exception) {
            Timber.w("createRoom:failure")
        }
    }

    override suspend fun fetchRoom(): Flow<List<String>> {
        return callbackFlow {
            try {
                val user = signInRepository.getOrSignInAnonymously()
                val snapshot = db.collection(COLLECTION_ROOMS)
                    .whereArrayContains(FIELD_PARTICIPANTS, user?.uid ?: "").get().await()
                val roomIds = mutableListOf<String>()
                for (document in snapshot) {
                    roomIds.add(document.id)
                }
                trySend(roomIds)
            } catch (e: Exception) {
                Timber.w("fetchRoom:failure")
            }
            awaitClose { /* nothing */ }
        }
    }

    override suspend fun sendMessage(roomId: String, message: Message) {

    }

    override suspend fun readMessage() {

    }
}