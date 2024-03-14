package com.rocky.whisper.ui.chat

import androidx.lifecycle.SavedStateHandle
import com.rocky.shared_test.MainCoroutineRule
import com.rocky.shared_test.data.repository.FakeMessageRepository
import com.rocky.whisper.WhisperDestinationsArgs.ROOM_ID_ARG
import com.rocky.whisper.data.Message
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ChatViewModelTest {

    private lateinit var chatViewModel: ChatViewModel

    private lateinit var messageRepository: FakeMessageRepository

    private var testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        messageRepository = FakeMessageRepository()

        chatViewModel = ChatViewModel(
            savedStateHandle = SavedStateHandle(mapOf(ROOM_ID_ARG to "0")),
            messageRepository = messageRepository,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun observeMessage_updateUiState() = runTest {
        val messages = listOf(
            Message("1", "0", "Hello", 0),
            Message("2", "0", "Hi", 0)
        )
        messageRepository.setMessages(messages)
        chatViewModel.observeMessage()

        advanceUntilIdle()

        assertEquals(messages, chatViewModel.uiState.value.messageList)
    }

}