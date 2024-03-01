package com.rocky.whisper.data

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val participants: List<String>,
    val timestamp: Long
)
