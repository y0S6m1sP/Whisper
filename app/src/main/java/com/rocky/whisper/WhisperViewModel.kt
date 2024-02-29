package com.rocky.whisper

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.rocky.whisper.data.SignInRepository
import com.rocky.whisper.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WhisperViewModel @Inject constructor(
    private val signInRepository: SignInRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    var userState = mutableStateOf<FirebaseUser?>(null)
        private set

    fun signInAnonymously() {
        viewModelScope.launch(dispatcher) {
            userState.value = signInRepository.signInAnonymously()
        }
    }

}