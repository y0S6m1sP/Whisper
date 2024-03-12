package com.rocky.whisper.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatroomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg chatroom: LocalChatroom)

    @Update
    suspend fun update(chatroom: LocalChatroom)

    @Delete
    suspend fun delete(chatroom: LocalChatroom)

    @Query("SELECT * FROM chatroom")
    fun observeAll(): Flow<List<LocalChatroom>>
}