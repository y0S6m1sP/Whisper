package com.rocky.whisper.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.rocky.whisper.R
import com.rocky.whisper.data.User
import com.rocky.whisper.data.source.local.UserDao
import com.rocky.whisper.data.toExternal
import com.rocky.whisper.data.toLocal
import com.rocky.whisper.di.ApplicationScope
import com.rocky.whisper.di.IoDispatcher
import com.rocky.whisper.util.Async
import com.rocky.whisper.util.RandomNameUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    private val userDao: UserDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
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

    override suspend fun observeUser(): Flow<User?> {
        // TODO: find better solution for first time login
        return userDao.observeUserById(auth.currentUser?.uid ?: "").map { it?.toExternal() }
    }

    override suspend fun uploadAvatar(data: ByteArray): Flow<Async<Unit>> {
        return callbackFlow {
            auth.currentUser?.uid?.let { uid ->
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
                            .set(hashMapOf("avatar" to downloadUrl.toString()), SetOptions.merge())
                            .addOnCompleteListener { updateProfileTask ->
                                if (updateProfileTask.isSuccessful) {
                                    scope.launch(dispatcher) {
                                        userDao.updateAvatar(uid, downloadUrl.toString())
                                    }
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