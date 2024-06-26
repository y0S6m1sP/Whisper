package com.rocky.whisper.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.rocky.whisper.data.WhisperDatabase
import com.rocky.whisper.data.chat.local.MessageDao
import com.rocky.whisper.data.chat.repository.DefaultMessageRepository
import com.rocky.whisper.data.chat.repository.MessageRepository
import com.rocky.whisper.data.home.local.ChatroomDao
import com.rocky.whisper.data.home.repository.ChatroomRepository
import com.rocky.whisper.data.home.repository.DefaultChatroomRepository
import com.rocky.whisper.data.user.local.UserDao
import com.rocky.whisper.data.user.repository.DefaultUserRepository
import com.rocky.whisper.data.user.repository.UserRepository
import com.rocky.whisper.feature.uploadavatar.util.DefaultImageCropper
import com.rocky.whisper.feature.uploadavatar.util.ImageCropper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMessageRepository(repository: DefaultMessageRepository): MessageRepository

    @Singleton
    @Binds
    abstract fun bindChatroomRepository(repository: DefaultChatroomRepository): ChatroomRepository

    @Singleton
    @Binds
    abstract fun bindUserRepository(repository: DefaultUserRepository): UserRepository
}


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return Firebase.storage
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): WhisperDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WhisperDatabase::class.java,
            "Whisper.db"
        ).build()
    }

    @Provides
    fun provideUserDao(database: WhisperDatabase): UserDao = database.userDao()

    @Provides
    fun provideChatroomDao(database: WhisperDatabase): ChatroomDao = database.chatroomDao()

    @Provides
    fun provideMessageDao(database: WhisperDatabase): MessageDao = database.messageDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilModule {

    @Singleton
    @Binds
    abstract fun bindImageCropper(imageCropper: DefaultImageCropper): ImageCropper
}