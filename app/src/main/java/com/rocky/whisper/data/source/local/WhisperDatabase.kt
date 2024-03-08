package com.rocky.whisper.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalUser::class], version = 1, exportSchema = false)
abstract class WhisperDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}