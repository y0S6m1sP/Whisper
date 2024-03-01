package com.rocky.whisper.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocky.whisper.data.repository.MessageRepository
import com.rocky.whisper.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isShowWhisperDialog: Boolean = false,
    val recentChatList: List<String> = listOf()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun createRoom(id: String) {
        viewModelScope.launch(dispatcher) {
            messageRepository.createRoom(id)
        }
    }

    fun fetchRoom() {
        viewModelScope.launch(dispatcher) {
            messageRepository.fetchRoom().collect {
                _uiState.update {currentState ->
                    currentState.copy(recentChatList = it)
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