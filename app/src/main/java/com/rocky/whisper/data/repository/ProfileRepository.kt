package com.rocky.whisper.data.repository

import com.rocky.whisper.data.Profile
import com.rocky.whisper.util.Async
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun fetchProfile(): Flow<Profile?>

    suspend fun uploadAvatar(data: ByteArray): Flow<Async<Unit>>
}