package com.rocky.whisper.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatroomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg chatroom: LocalChatroom)

    @Query("SELECT * FROM chatroom")
    fun observeAll(): Flow<List<LocalChatroom>>

    @Query("UPDATE chatroom SET lastMessage = :message, lastUpdate = :time WHERE id = :roomId")
    suspend fun updateLastMessage(roomId: String, message: String, time: Long)
}