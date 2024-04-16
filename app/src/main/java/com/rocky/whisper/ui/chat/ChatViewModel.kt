package com.rocky.whisper.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.rocky.whisper.WhisperDestinationsArgs.ROOM_ID_ARG
import com.rocky.whisper.data.Message
import com.rocky.whisper.data.repository.MessageRepository
import com.rocky.whisper.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

data class ChatUiState(
    val messageList: Map<LocalDate, List<Message>> = mapOf()
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val messageRepository: MessageRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val roomId: String = checkNotNull(savedStateHandle[ROOM_ID_ARG])

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    fun sendMessage(message: String) {
        viewModelScope.launch(dispatcher) {
            messageRepository.sendMessage(roomId, message)
        }
    }

    fun fetchMessage(): ListenerRegistration {
        return messageRepository.fetchMessage(roomId)
    }

    fun observeMessage() {
        viewModelScope.launch(dispatcher) {
            messageRepository.observeMessage(roomId).collectLatest {
                _uiState.update { currentState ->
                    currentState.copy(messageList = it.sortedBy { it.lastUpdate }
                        .groupBy { message ->
                            Date(message.lastUpdate)
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        })
                }
            }
        }
    }
}
