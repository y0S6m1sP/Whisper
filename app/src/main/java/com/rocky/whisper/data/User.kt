package com.rocky.whisper.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String? =null,
    val name: String? = null,
    val avatar: String? = null
)
