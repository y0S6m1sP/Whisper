package com.rocky.whisper.ui.uploadavatar

import com.rocky.shared_test.MainCoroutineRule
import com.rocky.shared_test.data.repository.FakeUserRepository
import com.rocky.shared_test.util.imagecropper.FakeImageCropper
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UploadAvatarViewModelTest {

    private lateinit var uploadAvatarViewModel: UploadAvatarViewModel

    private lateinit var userRepository: FakeUserRepository

    private lateinit var imageCropper: FakeImageCropper

    private var testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {

        userRepository = FakeUserRepository()

        imageCropper = FakeImageCropper()

        uploadAvatarViewModel = UploadAvatarViewModel(
            userRepository = userRepository,
            imageCropper = imageCropper,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun cropAndUploadAvatar_updateUiState() = runTest {
        uploadAvatarViewModel.cropAndUploadAvatar(view = mockk(relaxed = true), 0f)

        advanceUntilIdle()

        assertFalse(uploadAvatarViewModel.uiState.value.isUploading)
        assertTrue(uploadAvatarViewModel.uiState.value.isUploadComplete)
        assertTrue(uploadAvatarViewModel.uiState.value.isUploadSuccess)
    }
}