package com.rocky.whisper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rocky.whisper.data.home.local.ChatroomDao
import com.rocky.whisper.data.home.local.LocalChatroom
import com.rocky.whisper.data.chat.local.LocalMessage
import com.rocky.whisper.data.user.local.LocalUser
import com.rocky.whisper.data.chat.local.MessageDao
import com.rocky.whisper.data.user.local.UserDao

@Database(
    entities = [LocalUser::class, LocalChatroom::class, LocalMessage::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WhisperDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatroomDao(): ChatroomDao
    abstract fun messageDao(): MessageDao
}