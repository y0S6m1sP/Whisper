package com.rocky.whisper.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rocky.whisper.WhisperDestinationsArgs.ROOM_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val roomId: String = checkNotNull(savedStateHandle[ROOM_ID_ARG])
}