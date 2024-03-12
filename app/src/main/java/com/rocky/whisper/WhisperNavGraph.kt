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
import com.rocky.whisper.WhisperDestinationsArgs.OPPOSITE_USER_AVATAR_ARG
import com.rocky.whisper.WhisperDestinationsArgs.OPPOSITE_USER_NAME_ARG
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
                    onItemClick = { id, name, avatar ->
                        navActions.navigateToChat(id, name, avatar)
                    },
                    viewModel = viewModel
                )
            }
        }
        composable(SETTING_ROUTE) {
            val viewModel = hiltViewModel<SettingViewModel>()
            WhisperBottomAppBar(
                navigationActions = navActions,
                selectedTab = BottomAppBarTab.Setting
            ) {
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
            arguments = listOf(
                navArgument(OPPOSITE_USER_NAME_ARG) { nullable = true },
                navArgument(OPPOSITE_USER_AVATAR_ARG) { nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
        ) { entry ->
            val oppositeUserName = entry.arguments?.getString(OPPOSITE_USER_NAME_ARG)
            val oppositeUserAvatar = entry.arguments?.getString(OPPOSITE_USER_AVATAR_ARG)
            val viewModel = hiltViewModel<ChatViewModel>()
            ChatScreen(
                topAppBarTitle = oppositeUserName!!,
                onBackPressed = {
                    navController.popBackStack()
                },
                oppositeUserAvatar = oppositeUserAvatar!!,
                viewModel = viewModel
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