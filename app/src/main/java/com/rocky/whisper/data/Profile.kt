package com.rocky.whisper.data

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val name: String? = null,
    val avatar: String? = null
)
