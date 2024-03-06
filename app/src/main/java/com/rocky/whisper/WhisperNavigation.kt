package com.rocky.whisper

import androidx.navigation.NavHostController
import com.rocky.whisper.WhisperDestinations.HOME_ROUTE
import com.rocky.whisper.WhisperDestinations.SETTING_ROUTE
import com.rocky.whisper.WhisperDestinationsArgs.IMAGE_URI_ARG
import com.rocky.whisper.WhisperDestinationsArgs.ROOM_ID_ARG
import com.rocky.whisper.WhisperScreens.CHAT_SCREEN
import com.rocky.whisper.WhisperScreens.HOME_SCREEN
import com.rocky.whisper.WhisperScreens.SETTING_SCREEN
import com.rocky.whisper.WhisperScreens.UPLOAD_IMAGE_SCREEN

object WhisperScreens {
    const val HOME_SCREEN = "home"
    const val CHAT_SCREEN = "chat"
    const val SETTING_SCREEN = "setting"
    const val UPLOAD_IMAGE_SCREEN = "upload_image"
}

object WhisperDestinationsArgs {
    const val ROOM_ID_ARG = "roomId"
    const val IMAGE_URI_ARG = "image_uri"
}


object WhisperDestinations {
    const val HOME_ROUTE = HOME_SCREEN
    const val SETTING_ROUTE = SETTING_SCREEN
    const val CHAT_ROUTE = "$CHAT_SCREEN/{$ROOM_ID_ARG}"
    const val UPLOAD_IMAGE_ROUTE = "$UPLOAD_IMAGE_SCREEN?$IMAGE_URI_ARG={$IMAGE_URI_ARG}"
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

    fun navigateToUploadImage(uri: String) {
        navController.navigate("$UPLOAD_IMAGE_SCREEN?$IMAGE_URI_ARG=$uri")
    }

}