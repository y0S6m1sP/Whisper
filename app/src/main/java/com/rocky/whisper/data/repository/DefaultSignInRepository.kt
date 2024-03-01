package com.rocky.whisper.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class DefaultSignInRepository @Inject constructor(
    private val auth: FirebaseAuth
) : SignInRepository {
    override suspend fun getOrSignInAnonymously(): FirebaseUser? {
        auth.currentUser?.let {
            return it
        }

        return try {
            Timber.d("signInAnonymously:success")
            val task = auth.signInAnonymously().await()
            task.user
        } catch (e: Exception) {
            Timber.w("signInAnonymously:failure", e)
            null
        }
    }
}