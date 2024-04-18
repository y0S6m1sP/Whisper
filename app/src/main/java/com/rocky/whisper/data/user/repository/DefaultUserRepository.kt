package com.rocky.whisper.data.user.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rocky.whisper.R
import com.rocky.whisper.core.util.Async
import com.rocky.whisper.data.Constants.COLLECTION_ROOMS
import com.rocky.whisper.data.Constants.FIELD_USERS
import com.rocky.whisper.data.home.Chatroom
import com.rocky.whisper.data.user.User
import com.rocky.whisper.data.user.local.UserDao
import com.rocky.whisper.data.user.toExternal
import com.rocky.whisper.data.user.toLocal
import com.rocky.whisper.data.user.util.RandomNameUtils
import com.rocky.whisper.di.ApplicationScope
import com.rocky.whisper.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
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
        const val FIELD_AVATAR = "avatar"
    }

    override suspend fun signInAndCreateUser(): Flow<Async<Unit>> {
        return flow {
            auth.currentUser?.let {
                emit(Async.Success(Unit))
                return@flow
            }

            try {
                val task = auth.signInAnonymously().await()
                val newUser = User(task.user!!.uid, RandomNameUtils.generateRandomName(), "")
                db.collection(COLLECTION_USERS).document(task.user!!.uid).set(newUser).await()
                userDao.insertUser(newUser.toLocal())
                emit(Async.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Async.Error(R.string.error_sign_in_failure))
            }
        }
    }

    override suspend fun observeUser(): Flow<User?> {
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
                        updateAvatarToCurrentUser(
                            uid,
                            downloadUrl.toString(),
                            { trySend(Async.Success(Unit)) },
                            { trySend(Async.Error(R.string.error_update_profile_failure)) }
                        )
                        updateAvatarToChatroom(uid, downloadUrl.toString())

                    } else {
                        Timber.e(task.exception)
                        trySend(Async.Error(R.string.error_upload_avatar_failure))
                    }
                }
            }
            awaitClose {}
        }
    }

    private fun updateAvatarToCurrentUser(
        uid: String,
        downloadUrl: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        db.collection(COLLECTION_USERS).document(uid)
            .update(FIELD_AVATAR, downloadUrl)
            .addOnCompleteListener { updateProfileTask ->
                if (updateProfileTask.isSuccessful) {
                    scope.launch(dispatcher) {
                        userDao.updateAvatar(uid, downloadUrl)
                    }
                    onSuccess.invoke()
                } else {
                    onError.invoke()
                }
            }
    }

    private fun updateAvatarToChatroom(uid: String, downloadUrl: String) {
        db.collection(COLLECTION_ROOMS)
            .whereArrayContains(FIELD_USERS, auth.currentUser?.uid ?: "")
            .get().addOnSuccessListener { result ->
                result.documents.forEach { document ->
                    val chatroom = document.toObject(Chatroom::class.java)
                    chatroom?.let {
                        val updatedUsers = chatroom.userDetails?.map { user ->
                            if (user.id == uid) user.copy(avatar = downloadUrl) else user
                        }
                        val updatedChatroom = chatroom.copy(userDetails = updatedUsers)
                        db.collection(COLLECTION_ROOMS)
                            .document(chatroom.id!!)
                            .set(updatedChatroom)
                    }
                }
            }
    }
}