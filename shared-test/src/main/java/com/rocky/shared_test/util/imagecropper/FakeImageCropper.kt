package com.rocky.shared_test.util.imagecropper


import android.view.View
import androidx.compose.ui.geometry.Rect
import com.rocky.whisper.feature.uploadavatar.util.CropResult
import com.rocky.whisper.feature.uploadavatar.util.ImageCropper

class FakeImageCropper() : ImageCropper {
    override fun cropImage(view: View, bounds: Rect, onCropResult: (CropResult) -> Unit) {
        onCropResult.invoke(CropResult.Success(byteArrayOf()))
    }
}