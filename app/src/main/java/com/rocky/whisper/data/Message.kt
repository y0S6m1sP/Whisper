package com.rocky.whisper.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val senderId: String = "",
    val message: String = "",
    val timestamp: Long = 0L,
) {
    fun isCurrentUser(): Boolean {
        return senderId == Firebase.auth.uid
    }
}