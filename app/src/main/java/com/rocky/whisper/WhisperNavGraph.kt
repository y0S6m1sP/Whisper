package com.rocky.whisper

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rocky.whisper.WhisperDestinations.CHAT_ROUTE
import com.rocky.whisper.WhisperDestinations.HOME_ROUTE
import com.rocky.whisper.WhisperDestinations.SETTING_ROUTE
import com.rocky.whisper.WhisperDestinations.UPLOAD_IMAGE_ROUTE
import com.rocky.whisper.WhisperDestinationsArgs.IMAGE_URI_ARG
import com.rocky.whisper.ui.chat.ChatScreen
import com.rocky.whisper.ui.chat.ChatViewModel
import com.rocky.whisper.ui.home.HomeScreen
import com.rocky.whisper.ui.home.HomeViewModel
import com.rocky.whisper.ui.setting.SettingScreen
import com.rocky.whisper.ui.setting.SettingViewModel
import com.rocky.whisper.ui.uploadavatar.UploadAvatarScreen
import com.rocky.whisper.ui.uploadavatar.UploadAvatarViewModel
import com.rocky.whisper.util.component.BottomAppBarTab
import com.rocky.whisper.util.component.WhisperBottomAppBar

@Composable
fun WhisperNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navActions: WhisperNavigationActions = remember(navController) {
        WhisperNavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
        modifier = modifier,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() },
        route = "main"
    ) {
        composable(HOME_ROUTE) {
            val viewModel = hiltViewModel<HomeViewModel>()
            WhisperBottomAppBar(
                navigationActions = navActions,
                selectedTab = BottomAppBarTab.Home
            ) {
                HomeScreen(
                    onItemClick = {
                        navActions.navigateToChat(it)
                    },
                    viewModel = viewModel
                )
            }
        }
        composable(SETTING_ROUTE) {
            WhisperBottomAppBar(
                navigationActions = navActions,
                selectedTab = BottomAppBarTab.Setting
            ) {
                val viewModel = hiltViewModel<SettingViewModel>()
                SettingScreen(
                    viewModel = viewModel,
                    onImageSelect = { uri ->
                        navActions.navigateToUploadAvatar(uri.toString())
                    }
                )
            }
        }
        composable(
            CHAT_ROUTE,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
        ) {
            val viewModel = hiltViewModel<ChatViewModel>()
            ChatScreen(
                topAppBarTitle = viewModel.roomId,
                onBackPressed = {
                    navController.popBackStack()
                },
                viewModel
            )
        }
        composable(
            UPLOAD_IMAGE_ROUTE,
            arguments = listOf(navArgument(IMAGE_URI_ARG) { nullable = false })
        ) { entry ->
            val uri = entry.arguments?.getString(IMAGE_URI_ARG)
            val viewModel = hiltViewModel<UploadAvatarViewModel>()
            UploadAvatarScreen(
                uri!!,
                onBackPressed = { navController.popBackStack() },
                viewModel
            )
        }
    }
}