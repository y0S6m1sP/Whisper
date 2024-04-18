package com.rocky.shared_test.data.repository

import com.rocky.whisper.data.chat.Message
import com.rocky.whisper.data.chat.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMessageRepository: MessageRepository {

    var messageList: MutableList<Message> = mutableListOf()
        private set

    var messageCount = 0
        private set

    fun setMessages(messages: List<Message>){
        messageList.clear()
        messageList.addAll(messages)
    }

    fun setMessageCount(count: Int){
        messageCount = count
    }

    override suspend fun sendMessage(roomId: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun fetchMessage(roomId: String): com.google.firebase.firestore.ListenerRegistration {
        TODO("Not yet implemented")
    }

    override suspend fun observeMessage(roomId: String): Flow<List<Message>> {
        return flow {
            emit(messageList)
        }
    }

    override suspend fun observeMessageCount(): Flow<Int> {
        return flow {
            emit(messageCount)
        }
    }
}