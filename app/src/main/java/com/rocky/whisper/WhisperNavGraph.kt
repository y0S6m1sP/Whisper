package com.rocky.whisper

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
import com.rocky.whisper.home.HomeScreen
import com.rocky.whisper.home.HomeViewModel
import com.rocky.whisper.setting.SettingScreen
import com.rocky.whisper.setting.SettingViewModel
import com.rocky.whisper.util.BottomAppBarTab
import com.rocky.whisper.util.WhisperBottomAppBar

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
        startDestination = WhisperScreens.HOME,
        modifier = modifier,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() },
        route = "main"
    ) {
        composable(WhisperScreens.HOME) {
            val viewModel = hiltViewModel<HomeViewModel>()
            WhisperBottomAppBar(
                navigationActions = navActions,
                selectedTab = BottomAppBarTab.Home
            ) {
                HomeScreen(viewModel = viewModel)
            }
        }
        composable(WhisperScreens.SETTING) {
            WhisperBottomAppBar(
                navigationActions = navActions,
                selectedTab = BottomAppBarTab.Setting
            ) {
                val viewModel = hiltViewModel<SettingViewModel>()
                SettingScreen(viewModel = viewModel)
            }
        }
    }
}