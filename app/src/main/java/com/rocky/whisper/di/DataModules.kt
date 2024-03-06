package com.rocky.whisper.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.rocky.whisper.data.repository.DefaultMessageRepository
import com.rocky.whisper.data.repository.DefaultProfileRepository
import com.rocky.whisper.data.repository.DefaultSignInRepository
import com.rocky.whisper.data.repository.MessageRepository
import com.rocky.whisper.data.repository.ProfileRepository
import com.rocky.whisper.data.repository.SignInRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSignInRepository(repository: DefaultSignInRepository): SignInRepository

    @Singleton
    @Binds
    abstract fun bindMessageRepository(repository: DefaultMessageRepository): MessageRepository

    @Singleton
    @Binds
    abstract fun bindProfileRepository(repository: DefaultProfileRepository): ProfileRepository
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
