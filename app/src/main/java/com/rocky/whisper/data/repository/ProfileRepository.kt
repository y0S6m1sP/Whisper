package com.rocky.whisper.data.repository

import com.rocky.whisper.data.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun fetchProfile(): Flow<Profile?>

    suspend fun uploadImage(data: ByteArray): Flow<Boolean>
}