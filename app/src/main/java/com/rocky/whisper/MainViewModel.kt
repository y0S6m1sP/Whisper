package com.rocky.whisper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocky.whisper.core.util.Async
import com.rocky.whisper.data.home.repository.ChatroomRepository
import com.rocky.whisper.data.user.repository.UserRepository
import com.rocky.whisper.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatroomRepository: ChatroomRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    var isInitialized = false

    fun signInAnonymously(onSuccess: () -> Unit) {
        viewModelScope.launch(dispatcher) {
            userRepository.signInAndCreateUser().collectLatest {
                when (it) {
                    is Async.Success -> {
                        onSuccess.invoke()
                        fetchChatroom()
                        isInitialized = true
                    }

                    is Async.Error -> {
                        // TODO: handle error
                    }

                    is Async.Loading -> {

                    }
                }
            }
        }
    }

    private fun fetchChatroom() {
        viewModelScope.launch(dispatcher) {
            chatroomRepository.fetchChatroom()
        }
    }

}