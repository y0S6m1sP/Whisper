package com.rocky.whisper.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rocky.whisper.R
import com.rocky.whisper.util.WhisperTopAppBar

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = viewModel()
) {
    SettingContent()
}

@Composable
fun SettingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        WhisperTopAppBar(R.string.setting, false)
    }
}