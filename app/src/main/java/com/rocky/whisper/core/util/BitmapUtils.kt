package com.rocky.whisper.core.util

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}