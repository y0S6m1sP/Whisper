package com.rocky.whisper.data.repository

import com.google.firebase.firestore.ListenerRegistration
import com.rocky.whisper.data.Chatroom
import com.rocky.whisper.data.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun createRoom(id: String)

    fun fetchChatroom()

    suspend fun observeChatroom(): Flow<List<Chatroom>>

    suspend fun sendMessage(roomId: String, message: String)

    fun fetchMessage(roomId: String): ListenerRegistration

    suspend fun observeMessage(roomId: String): Flow<List<Message>>

    suspend fun observeMessageCount(): Flow<Int>
}