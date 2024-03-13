package com.rocky.shared_test.di

import android.content.Context
import androidx.room.Room
import com.rocky.whisper.data.source.local.WhisperDatabase
import com.rocky.whisper.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object DatabaseTestModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): WhisperDatabase {
        return Room
            .inMemoryDatabaseBuilder(context.applicationContext, WhisperDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}
