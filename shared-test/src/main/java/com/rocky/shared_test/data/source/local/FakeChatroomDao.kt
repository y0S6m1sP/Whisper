package com.rocky.shared_test.data.source.local

import com.rocky.whisper.data.home.local.ChatroomDao
import com.rocky.whisper.data.home.local.LocalChatroom
import com.rocky.whisper.data.user.User
import kotlinx.coroutines.flow.Flow

class FakeChatroomDao : ChatroomDao {
    override suspend fun insertAll(vararg chatroom: LocalChatroom) {
        TODO("Not yet implemented")
    }

    override suspend fun updateWithoutFirstVisibleIndex(
        id: String,
        userDetails: List<User>,
        lastMessage: String,
        lastUpdate: Long
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getChatroom(id: String): LocalChatroom? {
        TODO("Not yet implemented")
    }

    override suspend fun update(chatroom: LocalChatroom) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(chatroom: LocalChatroom) {
        TODO("Not yet implemented")
    }

    override fun observeAll(): Flow<List<LocalChatroom>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateFirstVisibleIndex(roomId: String, index: Int) {
        TODO("Not yet implemented")
    }
}