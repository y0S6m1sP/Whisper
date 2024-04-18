package com.rocky.shared_test.di

import com.rocky.shared_test.util.imagecropper.FakeImageCropper
import com.rocky.whisper.di.UtilModule
import com.rocky.whisper.feature.uploadavatar.util.ImageCropper
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UtilModule::class]
)
object UtilTestModule {

    @Singleton
    @Provides
    fun provideUserRepository(): ImageCropper {
        return FakeImageCropper()
    }
}