package com.rocky.whisper.data.repository

import com.rocky.whisper.data.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun createRoom(id: String)

    suspend fun fetchRoom(): Flow<List<String>>

    suspend fun sendMessage(roomId: String, message: String)

    suspend fun fetchMessage(roomId: String): Flow<List<Message>>
}