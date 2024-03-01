package com.rocky.whisper

import androidx.navigation.NavHostController
import com.rocky.whisper.WhisperDestinations.HOME_ROUTE
import com.rocky.whisper.WhisperDestinations.SETTING_ROUTE
import com.rocky.whisper.WhisperDestinationsArgs.ROOM_ID_ARG
import com.rocky.whisper.WhisperScreens.CHAT_SCREEN
import com.rocky.whisper.WhisperScreens.HOME_SCREEN
import com.rocky.whisper.WhisperScreens.SETTING_SCREEN

object WhisperScreens {
    const val HOME_SCREEN = "home"
    const val CHAT_SCREEN = "chat"
    const val SETTING_SCREEN = "setting"
}

object WhisperDestinationsArgs {
    const val ROOM_ID_ARG = "roomId"
}


object WhisperDestinations {
    const val HOME_ROUTE = HOME_SCREEN
    const val SETTING_ROUTE = SETTING_SCREEN
    const val CHAT_ROUTE = "$CHAT_SCREEN/{$ROOM_ID_ARG}"
}

class WhisperNavigationActions(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(HOME_ROUTE)
    }

    fun navigateToSetting() {
        navController.navigate(SETTING_ROUTE)
    }

    fun navigateToChat(roomId: String) {
        navController.navigate("$CHAT_SCREEN/$roomId")
    }

}