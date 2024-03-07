package com.rocky.whisper.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rocky.whisper.data.Profile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class DefaultProfileRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val signInRepository: SignInRepository,
) : ProfileRepository {

    companion object {
        const val COLLECTION_USERS = "users"
    }

    override suspend fun fetchProfile(): Flow<Profile?> {
        return callbackFlow {
            val user = signInRepository.getOrSignInAnonymously()
            val listener = user?.uid?.let { uid ->
                db.collection(COLLECTION_USERS).document(uid)
                    .addSnapshotListener { snapshot, error ->
                        error?.let {
                            Timber.e(error)
                            return@addSnapshotListener
                        }

                        trySend(snapshot?.toObject(Profile::class.java))
                    }
            }
            awaitClose { listener?.remove() }
        }
    }

    override suspend fun uploadAvatar(data: ByteArray): Flow<Boolean> {
        return callbackFlow {
            val user = signInRepository.getOrSignInAnonymously()
            user?.uid?.let { uid ->
                val avatarRef = storage.reference.child("$uid.jpg")
                val uploadTask = avatarRef.putBytes(data)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    avatarRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        db.collection(COLLECTION_USERS).document(uid)
                            .set(hashMapOf("avatar" to downloadUrl.toString()))
                            .addOnCompleteListener { updateProfileTask ->
                                if (updateProfileTask.isSuccessful) {
                                    trySend(true)
                                } else {
                                    trySend(false)
                                }
                            }
                    } else {
                        Timber.e(task.exception)
                        trySend(false)
                    }
                }
            }
            awaitClose {}
        }
    }
}