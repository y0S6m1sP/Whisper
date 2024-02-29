package com.rocky.whisper.data

import com.google.firebase.auth.FirebaseUser

interface SignInRepository {

    suspend fun signInAnonymously(): FirebaseUser?
}