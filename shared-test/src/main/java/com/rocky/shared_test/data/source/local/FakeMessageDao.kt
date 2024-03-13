package com.rocky.shared_test.data.source.local

import com.rocky.whisper.data.source.local.LocalMessage
import com.rocky.whisper.data.source.local.MessageDao
import kotlinx.coroutines.flow.Flow

class FakeMessageDao: MessageDao{
    override suspend fun insertAll(vararg message: LocalMessage) {
        TODO("Not yet implemented")
    }

    override suspend fun update(id: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun delete(message: LocalMessage) {
        TODO("Not yet implemented")
    }

    override fun observeAll(roomId: String): Flow<List<LocalMessage>> {
        TODO("Not yet implemented")
    }

    override fun observeMessageCount(): Flow<Int> {
        TODO("Not yet implemented")
    }
}