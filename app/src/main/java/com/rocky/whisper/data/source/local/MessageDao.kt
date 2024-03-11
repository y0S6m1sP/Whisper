package com.rocky.whisper.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg message: LocalMessage)

    @Query("SELECT * FROM message WHERE roomId = :roomId")
    fun observeAll(roomId: String): Flow<List<LocalMessage>>
}