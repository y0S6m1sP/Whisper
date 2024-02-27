package com.rocky.whisper

import androidx.navigation.NavHostController
import com.rocky.whisper.WhisperRoutes.HOME_ROUTE
import com.rocky.whisper.WhisperRoutes.SETTING_ROUTE
import com.rocky.whisper.WhisperScreens.HOME
import com.rocky.whisper.WhisperScreens.SETTING

object WhisperScreens {
    const val HOME = "home"
    const val MESSAGE = "message"
    const val SETTING = "setting"
}

object WhisperRoutes {
    const val HOME_ROUTE = HOME
    const val SETTING_ROUTE = SETTING
}

class WhisperNavigationActions(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(HOME_ROUTE)
    }

    fun navigateToSetting() {
        navController.navigate(SETTING_ROUTE)
    }

}