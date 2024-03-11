package com.rocky.whisper.data.repository

import com.rocky.whisper.data.User
import com.rocky.whisper.util.Async
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun signInAndCreateUser()
    suspend fun observeUser(): Flow<User?>
    suspend fun uploadAvatar(data: ByteArray): Flow<Async<Unit>>
}