package com.rocky.whisper.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocky.whisper.data.home.Chatroom
import com.rocky.whisper.data.user.User
import com.rocky.whisper.data.chat.repository.MessageRepository
import com.rocky.whisper.data.home.repository.ChatroomRepository
import com.rocky.whisper.data.user.repository.UserRepository
import com.rocky.whisper.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val user: User? = null,
    val isShowWhisperDialog: Boolean = false,
    val recentChatList: List<Chatroom> = listOf(),
    val messageCount: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatroomRepository: ChatroomRepository,
    private val messageRepository: MessageRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun observeUser() {
        viewModelScope.launch(dispatcher) {
            userRepository.observeUser().collectLatest {
                _uiState.update { currentState ->
                    currentState.copy(user = it)
                }
            }
        }
    }

    fun createRoom(id: String) {
        viewModelScope.launch(dispatcher) {
            chatroomRepository.createRoom(id)
        }
    }

    fun observeChatroom() {
        viewModelScope.launch(dispatcher) {
            chatroomRepository.observeChatroom().collect {
                _uiState.update { currentState ->
                    currentState.copy(recentChatList = it)
                }
            }
        }
    }

    fun observeMessageCount() {
        viewModelScope.launch(dispatcher) {
            messageRepository.observeMessageCount().collect {
                _uiState.update { currentState ->
                    currentState.copy(messageCount = it)
                }
            }
        }
    }

    fun showWhisperDialog() {
        _uiState.update { currentState ->
            currentState.copy(isShowWhisperDialog = true)
        }
    }

    fun hideWhisperDialog() {
        _uiState.update { currentState ->
            currentState.copy(isShowWhisperDialog = false)
        }
    }
}