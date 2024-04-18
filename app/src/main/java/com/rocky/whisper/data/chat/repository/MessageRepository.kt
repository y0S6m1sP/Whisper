package com.rocky.whisper.data.chat.repository

import com.google.firebase.firestore.ListenerRegistration
import com.rocky.whisper.data.chat.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun sendMessage(roomId: String, message: String)

    fun fetchMessage(roomId: String): ListenerRegistration

    suspend fun observeMessage(roomId: String): Flow<List<Message>>

    suspend fun observeMessageCount(): Flow<Int>
}