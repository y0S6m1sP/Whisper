package com.rocky.whisper.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.Serializable

@Serializable
data class Chatroom(
    val id: String? = null,
    val users: List<String>? = null,
    val userDetails: List<User>? = null,
    val lastMessage: String? = null,
    val firstVisibleIndex: Int? = null,
    val lastUpdate: Long? = null
) {
    fun parseOppositeUser(): User? {
        return userDetails?.firstOrNull { it.id != Firebase.auth.currentUser?.uid }
    }
}
