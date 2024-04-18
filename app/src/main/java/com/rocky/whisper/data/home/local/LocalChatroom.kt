package com.rocky.whisper.data.home.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rocky.whisper.data.user.User

@Entity(tableName = "chatroom")
data class LocalChatroom(
    @PrimaryKey val id: String,
    val users: List<String>,
    val userDetails: List<User>,
    val lastMessage: String = "",
    val firstVisibleIndex: Int = 0,
    val lastUpdate: Long
)
