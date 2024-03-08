package com.rocky.whisper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocky.whisper.data.repository.DefaultUserRepository
import com.rocky.whisper.data.repository.UserRepository
import com.rocky.whisper.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WhisperViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    fun signInAnonymously() {
        viewModelScope.launch(dispatcher) {
            userRepository.signInAndCreateUser()
        }
    }

}