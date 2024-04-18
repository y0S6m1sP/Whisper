package com.rocky.shared_test.data.repository

import com.rocky.whisper.data.home.Chatroom
import com.rocky.whisper.data.home.repository.ChatroomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeChatroomRepository : ChatroomRepository {

    var recentChatList: MutableList<Chatroom> = mutableListOf()
        private set

    override fun createRoom(id: String) {
        val chatroom = Chatroom(id)
        recentChatList.add(chatroom)
    }

    override fun fetchChatroom() {
        TODO("Not yet implemented")
    }

    override suspend fun observeChatroom(): Flow<List<Chatroom>> {
        return flow {
            emit(recentChatList)
        }
    }

    override suspend fun updateFirstVisibleIndex(roomId: String, index: Int) {
        TODO("Not yet implemented")
    }
}