package com.rocky.whisper.util

import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import androidx.compose.ui.geometry.Rect
import java.io.ByteArrayOutputStream

object BitmapUtils {

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun cropImage(view: View, bounds: Rect, onCropSuccess: (ByteArray) -> Unit) {
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
                        onCropSuccess.invoke(bitmapToByteArray(bitmap))
                    }
                }
            }, Handler(Looper.getMainLooper())
        )
    }
}