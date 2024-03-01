package com.rocky.whisper.data.repository

import com.google.firebase.auth.FirebaseUser

interface SignInRepository {

    suspend fun getOrSignInAnonymously(): FirebaseUser?
}