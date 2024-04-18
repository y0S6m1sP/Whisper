package com.rocky.whisper.data.chat.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class LocalMessage(
    @PrimaryKey val id: String,
    val roomId: String,
    val senderId: String,
    val message: String,
    val lastUpdate: Long,
)
