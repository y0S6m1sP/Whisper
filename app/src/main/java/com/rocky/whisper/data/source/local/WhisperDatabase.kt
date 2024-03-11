package com.rocky.whisper.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

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