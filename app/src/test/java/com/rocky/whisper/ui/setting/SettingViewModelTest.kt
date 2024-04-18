package com.rocky.whisper.ui.setting

import com.rocky.shared_test.MainCoroutineRule
import com.rocky.shared_test.data.repository.FakeUserRepository
import com.rocky.whisper.data.user.User
import com.rocky.whisper.feature.setting.SettingViewModel
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingViewModelTest {

    private lateinit var settingViewModel: SettingViewModel

    private lateinit var userRepository: FakeUserRepository

    private var testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        userRepository = FakeUserRepository()

        settingViewModel = SettingViewModel(
            userRepository = userRepository,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun observeUser_updateUiState() = runTest {
        val user = User("fakeId", "fakeName", "fakeAvatar")
        userRepository.setUser(user)
        settingViewModel.observeUser()

        advanceUntilIdle()

        TestCase.assertEquals(user.avatar, settingViewModel.uiState.value.avatar)
    }

}