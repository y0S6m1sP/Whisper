package com.rocky.shared_test.util.imagecropper


import android.view.View
import androidx.compose.ui.geometry.Rect
import com.rocky.whisper.util.imagecropper.ImageCropper

class FakeImageCropper() : ImageCropper {

    override fun cropImage(
        view: View,
        bounds: Rect,
        onCropSuccess: (ByteArray) -> Unit
    ) {
        onCropSuccess.invoke(byteArrayOf())
    }
}