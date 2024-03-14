package com.rocky.shared_test.data.repository

import com.rocky.whisper.data.User
import com.rocky.whisper.data.repository.UserRepository
import com.rocky.whisper.util.Async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserRepository: UserRepository {

    private var user: User? = null

    fun setUser(user: User) {
        this.user = user
    }

    override suspend fun signInAndCreateUser(): Flow<Async<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun observeUser(): Flow<User?> {
        return flow {
            emit(user)
        }
    }

    override suspend fun uploadAvatar(data: ByteArray): Flow<Async<Unit>> {
        return flow {
            emit(Async.Success(Unit))
        }
    }
}