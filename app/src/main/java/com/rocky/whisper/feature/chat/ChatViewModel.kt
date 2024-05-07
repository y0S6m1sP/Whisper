package com.rocky.whisper.feature.chat

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.rocky.whisper.WhisperDestinationsArgs.FIRST_VISIBLE_INDEX_ARG
import com.rocky.whisper.WhisperDestinationsArgs.ROOM_ID_ARG
import com.rocky.whisper.data.chat.Message
import com.rocky.whisper.data.chat.repository.MessageRepository
import com.rocky.whisper.data.home.repository.ChatroomRepository
import com.rocky.whisper.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class ChatUiState(
    val messageList: Map<LocalDate, List<Message>> = mapOf()
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatroomRepository: ChatroomRepository,
    private val messageRepository: MessageRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val roomId: String = checkNotNull(savedStateHandle[ROOM_ID_ARG])
    val firstVisibleIndex: String = checkNotNull(savedStateHandle[FIRST_VISIBLE_INDEX_ARG])

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    fun sendMessage(message: String) {
        viewModelScope.launch(dispatcher) {
            messageRepository.sendMessage(roomId, message)
        }
    }

    fun sendImage(uri: Uri, image: ByteArray) {
        viewModelScope.launch(dispatcher) {
            messageRepository.sendImage(roomId, uri, image).collectLatest {

            }
        }
    }

    fun fetchMessage(): ListenerRegistration {
        return messageRepository.fetchMessage(roomId)
    }

    fun observeMessage() {
        viewModelScope.launch(dispatcher) {
            messageRepository.observeMessage(roomId).collectLatest {
                _uiState.update { currentState ->
                    currentState.copy(messageList = it)
                }
            }
        }
    }

    fun updateFirstVisibleIndex(index: Int) {
        viewModelScope.launch(dispatcher) {
            chatroomRepository.updateFirstVisibleIndex(roomId, index)
        }
    }
}
