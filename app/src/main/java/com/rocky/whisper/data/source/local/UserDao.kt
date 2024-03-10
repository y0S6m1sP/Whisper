package com.rocky.whisper.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: LocalUser)

    @Update
    suspend fun updateUser(user: LocalUser)

    @Query("SELECT * FROM user WHERE id = :userId")
    fun observeUserById(userId: String): Flow<LocalUser>

    @Query("UPDATE user SET avatar = :avatar WHERE id = :userId")
    suspend fun updateAvatar(userId: String, avatar: String)
}