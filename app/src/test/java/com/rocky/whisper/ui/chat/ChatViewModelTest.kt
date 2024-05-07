package com.rocky.whisper.ui.chat

import androidx.lifecycle.SavedStateHandle
import com.rocky.shared_test.MainCoroutineRule
import com.rocky.shared_test.data.repository.FakeChatroomRepository
import com.rocky.shared_test.data.repository.FakeMessageRepository
import com.rocky.whisper.WhisperDestinationsArgs.FIRST_VISIBLE_INDEX_ARG
import com.rocky.whisper.WhisperDestinationsArgs.ROOM_ID_ARG
import com.rocky.whisper.data.chat.Message
import com.rocky.whisper.feature.chat.ChatViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.ZoneId
import java.util.Date

@ExperimentalCoroutinesApi
class ChatViewModelTest {

    private lateinit var chatViewModel: ChatViewModel

    private lateinit var chatroomRepository: FakeChatroomRepository
    private lateinit var messageRepository: FakeMessageRepository

    private var testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        chatroomRepository = FakeChatroomRepository()
        messageRepository = FakeMessageRepository()

        chatViewModel = ChatViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    ROOM_ID_ARG to "0",
                    FIRST_VISIBLE_INDEX_ARG to "0"
                )
            ),
            chatroomRepository = chatroomRepository,
            messageRepository = messageRepository,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun observeMessage_updateUiState() = runTest {
        val messages = listOf(
            Message("1", "0", "Hello", "", 0),
            Message("2", "0", "Hi", "", 0)
        )
        messageRepository.setMessages(messages)
        chatViewModel.observeMessage()

        advanceUntilIdle()

        assertEquals(messages.groupBy { message ->
            Date(message.lastUpdate)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }, chatViewModel.uiState.value.messageList)
    }

}