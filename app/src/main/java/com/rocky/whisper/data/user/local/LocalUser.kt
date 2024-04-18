package com.rocky.whisper.data.user.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class LocalUser(
    @PrimaryKey val id: String,
    val name: String,
    val avatar: String = ""
)
