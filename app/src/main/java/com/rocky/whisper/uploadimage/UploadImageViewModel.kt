package com.rocky.whisper.uploadimage

import android.view.View
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocky.whisper.data.repository.ProfileRepository
import com.rocky.whisper.di.IoDispatcher
import com.rocky.whisper.util.BitmapUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isUploadFinish: Boolean = false
)

@HiltViewModel
class UploadImageViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun cropAndUploadImage(view: View, padding: Float) {
        val bounds = Rect(Offset(view.pivotX, view.pivotY), view.width / 2 - padding)
        BitmapUtils.cropImage(view, bounds) {
            viewModelScope.launch(dispatcher) {
                profileRepository.uploadImage(it).collectLatest {
                    _uiState.update { currentState ->
                        currentState.copy(isUploadFinish = it)
                    }
                }
            }
        }
    }
}