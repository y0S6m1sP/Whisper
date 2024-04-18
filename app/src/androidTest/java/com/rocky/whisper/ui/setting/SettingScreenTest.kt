package com.rocky.whisper.ui.setting

import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.rocky.whisper.HiltTestActivity
import com.rocky.whisper.R
import com.rocky.whisper.data.user.repository.UserRepository
import com.rocky.whisper.feature.setting.SettingScreen
import com.rocky.whisper.feature.setting.SettingViewModel
import com.rocky.whisper.core.theme.WhisperTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class SettingScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    private val activity get() = composeTestRule.activity

    @Inject
    lateinit var repository: UserRepository

    private var testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        hiltRule.inject()

        composeTestRule.setContent {
            WhisperTheme {
                Surface {
                    SettingScreen(
                        viewModel = SettingViewModel(userRepository = repository, testDispatcher),
                        onImageSelect = {},
                    )
                }
            }
        }
    }

    @Test
    fun settingScreen_isUiDisplayedCorrect() {
        composeTestRule.onNodeWithText(activity.getString(R.string.setting)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.app_theme)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.notifications)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.report_problem)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.help)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.delete_account)).assertIsDisplayed()
    }
}