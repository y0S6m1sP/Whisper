package com.rocky.whisper.data.chat

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String = "",
    val senderId: String = "",
    val message: String = "",
    val lastUpdate: Long = 0L,
) {
    fun isCurrentUser(): Boolean {
        return senderId == Firebase.auth.uid
    }

    fun isImageUrl(): Boolean {
        return message.contains("firebasestorage")
    }
}