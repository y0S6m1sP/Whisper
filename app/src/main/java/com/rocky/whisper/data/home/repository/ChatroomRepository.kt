package com.rocky.whisper.data.home.repository

import com.rocky.whisper.data.home.Chatroom
import kotlinx.coroutines.flow.Flow

interface ChatroomRepository {

    fun createRoom(id: String)

    fun fetchChatroom()

    suspend fun observeChatroom(): Flow<List<Chatroom>>

    suspend fun updateFirstVisibleIndex(roomId: String, index: Int)
}