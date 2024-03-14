package com.rocky.whisper.util.imagecropper

sealed class CropResult {
    data class Success(val byteArray: ByteArray) : CropResult()
    object Failure : CropResult()
}