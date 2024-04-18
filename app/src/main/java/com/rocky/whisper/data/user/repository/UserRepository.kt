package com.rocky.whisper.data.user.repository

import com.rocky.whisper.data.user.User
import com.rocky.whisper.core.util.Async
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun signInAndCreateUser(): Flow<Async<Unit>>
    suspend fun observeUser(): Flow<User?>
    suspend fun uploadAvatar(data: ByteArray): Flow<Async<Unit>>
}