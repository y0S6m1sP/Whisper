package com.rocky.whisper.ui.uploadavatar

import android.view.View
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocky.whisper.data.repository.ProfileRepository
import com.rocky.whisper.di.IoDispatcher
import com.rocky.whisper.util.Async
import com.rocky.whisper.util.BitmapUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UploadAvatarUiState(
    val isUploading: Boolean = false,
    val isUploadSuccess: Boolean = false,
    val isUploadComplete: Boolean = false
)

@HiltViewModel
class UploadAvatarViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(UploadAvatarUiState())
    val uiState: StateFlow<UploadAvatarUiState> = _uiState

    fun cropAndUploadAvatar(view: View, padding: Float) {
        val bounds = Rect(Offset(view.pivotX, view.pivotY), view.width / 2 - padding)
        BitmapUtils.cropImage(view, bounds) {
            viewModelScope.launch(dispatcher) {
                profileRepository.uploadAvatar(it).collectLatest {
                    produceUploadAvatarUiState(it)
                }
            }
        }
    }

    private fun produceUploadAvatarUiState(task: Async<Unit>) {
        _uiState.update { currentState ->
            when (task) {
                is Async.Error -> {
                    currentState.copy(
                        isUploading = false,
                        isUploadSuccess = false,
                        isUploadComplete = true
                    )
                }

                Async.Loading -> {
                    currentState.copy(isUploading = true)
                }

                is Async.Success -> {
                    currentState.copy(
                        isUploading = false,
                        isUploadSuccess = true,
                        isUploadComplete = true
                    )
                }
            }
        }
    }
}