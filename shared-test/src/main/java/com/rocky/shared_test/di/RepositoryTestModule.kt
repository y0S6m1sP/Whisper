package com.rocky.shared_test.di

import com.rocky.shared_test.data.repository.FakeMessageRepository
import com.rocky.shared_test.data.repository.FakeUserRepository
import com.rocky.whisper.data.repository.MessageRepository
import com.rocky.whisper.data.repository.UserRepository
import com.rocky.whisper.di.RepositoryModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object RepositoryTestModule {

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository {
        return FakeUserRepository()
    }

    @Singleton
    @Provides
    fun provideMessageRepository(): MessageRepository {
        return FakeMessageRepository()
    }
}