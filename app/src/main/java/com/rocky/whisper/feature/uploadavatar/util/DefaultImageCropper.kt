package com.rocky.whisper.feature.uploadavatar.util

import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import androidx.compose.ui.geometry.Rect
import com.rocky.whisper.core.util.bitmapToByteArray
import javax.inject.Inject

class DefaultImageCropper @Inject constructor() : ImageCropper {

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