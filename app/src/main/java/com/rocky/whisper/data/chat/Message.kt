package com.rocky.whisper.data.chat

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String = "",
    val senderId: String = "",
    val message: String = "",
    val image: String = "",
    val lastUpdate: Long = 0L,
)

fun Message.isCurrentUser(): Boolean {
    return senderId == Firebase.auth.uid
}

fun Message.isImage(): Boolean {
    return image.isNotEmpty()
}

fun Message.isImageLoading(): Boolean {
    return message.isEmpty() && image.isEmpty()
}