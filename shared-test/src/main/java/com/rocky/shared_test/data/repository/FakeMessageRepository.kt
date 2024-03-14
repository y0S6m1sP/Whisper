package com.rocky.shared_test.data.repository

import com.rocky.whisper.data.Chatroom
import com.rocky.whisper.data.Message
import com.rocky.whisper.data.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMessageRepository: MessageRepository {

    var messageList: MutableList<Message> = mutableListOf()
        private set
    var recentChatList: MutableList<Chatroom> = mutableListOf()
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