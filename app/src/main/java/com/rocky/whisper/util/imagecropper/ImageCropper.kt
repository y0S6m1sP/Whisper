package com.rocky.whisper.util.imagecropper

import android.view.View
import androidx.compose.ui.geometry.Rect

interface ImageCropper {
    fun cropImage(view: View, bounds: Rect, onCropResult: (CropResult) -> Unit)
}