package com.rocky.shared_test.data.source.local

import com.rocky.whisper.data.source.local.LocalUser
import com.rocky.whisper.data.source.local.UserDao
import kotlinx.coroutines.flow.Flow

class FakeUserDao : UserDao {
    override suspend fun insertUser(user: LocalUser) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: LocalUser) {
        TODO("Not yet implemented")
    }

    override fun observeUserById(userId: String): Flow<LocalUser?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAvatar(userId: String, avatar: String) {
        TODO("Not yet implemented")
    }
}