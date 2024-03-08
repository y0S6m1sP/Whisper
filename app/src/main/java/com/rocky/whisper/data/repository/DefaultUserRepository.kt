package com.rocky.whisper.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rocky.whisper.R
import com.rocky.whisper.data.User
import com.rocky.whisper.data.source.local.UserDao
import com.rocky.whisper.data.toLocal
import com.rocky.whisper.util.Async
import com.rocky.whisper.util.RandomNameUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    private val userDao: UserDao
) : UserRepository {

    companion object {
        const val COLLECTION_USERS = "users"
    }

    override suspend fun signInAndCreateUser() {
        auth.currentUser?.let {
            return
        }

        try {
            val task = auth.signInAnonymously().await()
            val newUser = User(task.user!!.uid, RandomNameUtils.generateRandomName(), "")
            db.collection(COLLECTION_USERS).document(task.user!!.uid).set(newUser).await()
            userDao.insertUser(newUser.toLocal())
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun fetchProfile(): Flow<User?> {
        return callbackFlow {
            val user = auth.currentUser
            val listener = user?.uid?.let { uid ->
                db.collection(COLLECTION_USERS).document(uid)
                    .addSnapshotListener { snapshot, error ->
                        error?.let {
                            Timber.e(error)
                            return@addSnapshotListener
                        }

                        trySend(snapshot?.toObject(User::class.java))
                    }
            }
            awaitClose { listener?.remove() }
        }
    }

    override suspend fun uploadAvatar(data: ByteArray): Flow<Async<Unit>> {
        return callbackFlow {
            val user = auth.currentUser
            user?.uid?.let { uid ->
                trySend(Async.Loading)
                val avatarRef = storage.reference.child("$uid.jpg")
                val uploadTask = avatarRef.putBytes(data)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    avatarRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        db.collection(COLLECTION_USERS).document(uid)
                            .set(hashMapOf("avatar" to downloadUrl.toString()))
                            .addOnCompleteListener { updateProfileTask ->
                                if (updateProfileTask.isSuccessful) {
                                    trySend(Async.Success(Unit))
                                } else {
                                    trySend(Async.Error(R.string.error_update_profile_failure))
                                }
                            }
                    } else {
                        Timber.e(task.exception)
                        trySend(Async.Error(R.string.error_upload_avatar_failure))
                    }
                }
            }
            awaitClose {}
        }
    }
}