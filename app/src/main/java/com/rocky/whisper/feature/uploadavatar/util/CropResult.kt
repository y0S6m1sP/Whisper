package com.rocky.whisper.feature.uploadavatar.util

sealed class CropResult {
    data class Success(val byteArray: ByteArray) : CropResult()
    object Failure : CropResult()
}