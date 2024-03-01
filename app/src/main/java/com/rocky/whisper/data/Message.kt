package com.rocky.whisper.data

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val senderId: String,
    val message: String,
    val timestamp: Long
)