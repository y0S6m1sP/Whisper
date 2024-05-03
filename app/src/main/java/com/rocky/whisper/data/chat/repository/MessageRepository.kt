package com.rocky.whisper.data.chat.repository

import com.google.firebase.firestore.ListenerRegistration
import com.rocky.whisper.data.chat.Message
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MessageRepository {

    suspend fun sendMessage(roomId: String, message: String)

    fun fetchMessage(roomId: String): ListenerRegistration

    suspend fun observeMessage(roomId: String): Flow<Map<LocalDate, List<Message>>>

    suspend fun observeMessageCount(): Flow<Int>
}