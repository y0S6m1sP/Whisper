package com.rocky.whisper.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: LocalUser)

    @Update
    suspend fun updateUser(user: LocalUser)

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserById(userId: String): LocalUser?
}