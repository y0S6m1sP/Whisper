package com.rocky.shared_test.data.repository

import android.net.Uri
import com.rocky.whisper.core.util.Async
import com.rocky.whisper.data.chat.Message
import com.rocky.whisper.data.chat.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class FakeMessageRepository : MessageRepository {

    var messageList: MutableList<Message> = mutableListOf()
        private set

    var messageCount = 0
        private set

    fun setMessages(messages: List<Message>) {
        messageList.clear()
        messageList.addAll(messages)
    }

    fun setMessageCount(count: Int) {
        messageCount = count
    }

    override suspend fun sendMessage(roomId: String, message: String) {
        TODO("Not yet implemented")
    }

    override suspend fun sendImage(roomId: String, uri: Uri, image: ByteArray): Flow<Async<Unit>> {
        TODO("Not yet implemented")
    }

    override fun fetchMessage(roomId: String): com.google.firebase.firestore.ListenerRegistration {
        TODO("Not yet implemented")
    }

    override suspend fun observeMessage(roomId: String): Flow<Map<LocalDate, List<Message>>> {
        return flow {
            emit(messageList.sortedBy { message -> message.lastUpdate }
                .groupBy { message ->
                    Date(message.lastUpdate)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
            )
        }
    }

    override suspend fun observeMessageCount(): Flow<Int> {
        return flow {
            emit(messageCount)
        }
    }
}