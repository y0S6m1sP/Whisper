package com.rocky.whisper.data.home.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rocky.whisper.data.user.User
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatroomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg chatroom: LocalChatroom)

    @Query("UPDATE chatroom SET userDetails = :userDetails, lastMessage = :lastMessage, lastUpdate = :lastUpdate WHERE id = :id")
    suspend fun updateWithoutFirstVisibleIndex(
        id: String,
        userDetails: List<User>,
        lastMessage: String,
        lastUpdate: Long
    )

    @Query("SELECT * FROM chatroom WHERE id = :id")
    suspend fun getChatroom(id: String): LocalChatroom?

    @Update
    suspend fun update(chatroom: LocalChatroom)

    @Delete
    suspend fun delete(chatroom: LocalChatroom)

    @Query("SELECT * FROM chatroom")
    fun observeAll(): Flow<List<LocalChatroom>>

    @Query("UPDATE chatroom SET firstVisibleIndex = :index WHERE id = :roomId")
    suspend fun updateFirstVisibleIndex(roomId: String, index: Int)
}