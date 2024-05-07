package com.rocky.whisper.data.chat.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg message: LocalMessage)

    @Query("UPDATE message SET message = :message, image = :image WHERE id = :id")
    suspend fun update(id: String, message: String, image: String)

    @Delete
    fun delete(message: LocalMessage)

    @Query("SELECT * FROM message WHERE roomId = :roomId")
    fun observeAll(roomId: String): Flow<List<LocalMessage>>

    @Query("SELECT COUNT(*) FROM message")
    fun observeMessageCount(): Flow<Int>
}