package com.rocky.whisper.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class LocalUser(
    @PrimaryKey val id: String,
    val name: String,
    val avatar: String = ""
)
