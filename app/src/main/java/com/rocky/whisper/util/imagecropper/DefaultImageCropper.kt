package com.rocky.whisper.util.imagecropper

import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import androidx.compose.ui.geometry.Rect
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class DefaultImageCropper @Inject constructor() : ImageCropper {

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    override fun cropImage(view: View, bounds: Rect, onCropResult: (CropResult) -> Unit) {
        val bitmap = Bitmap.createBitmap(
            bounds.width.toInt(),
            bounds.height.toInt(),
            Bitmap.Config.ARGB_8888
        )
        PixelCopy.request(
            (view.context as Activity).window,
            android.graphics.Rect(
                bounds.left.toInt(),
                bounds.top.toInt(),
                bounds.right.toInt(),
                bounds.bottom.toInt()
            ),
            bitmap, {
                when (it) {
                    PixelCopy.SUCCESS -> {
                        onCropResult.invoke(CropResult.Success(bitmapToByteArray(bitmap)))
                    }
                    else -> {
                        onCropResult.invoke(CropResult.Failure)
                    }
                }
            }, Handler(Looper.getMainLooper())
        )
    }
}