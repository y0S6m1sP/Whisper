package com.rocky.whisper.feature.uploadavatar.util

import android.view.View
import androidx.compose.ui.geometry.Rect

interface ImageCropper {
    fun cropImage(view: View, bounds: Rect, onCropResult: (CropResult) -> Unit)
}