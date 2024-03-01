package com.rocky.whisper.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rocky.whisper.R
import com.rocky.whisper.util.LogoTopAppBar

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel
) {
    SettingContent()
}

@Composable
fun SettingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        LogoTopAppBar(R.string.setting, false)
    }
}