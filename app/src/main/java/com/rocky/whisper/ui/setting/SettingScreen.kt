package com.rocky.whisper.ui.setting

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rocky.whisper.R
import com.rocky.whisper.util.component.Avatar
import com.rocky.whisper.util.component.LogoTopAppBar

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel,
    onImageSelect: (uri: Uri) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.observeUser()
    }

    SettingContent(
        avatar = uiState.avatar,
        onImageSelect = onImageSelect,
        modifier = modifier
    )
}

@Composable
fun SettingContent(
    avatar: String,
    onImageSelect: (uri: Uri) -> Unit,
    modifier: Modifier = Modifier
) {

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let(onImageSelect) }

    fun launchPhotoPicker() {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoTopAppBar(stringResource(id = R.string.setting))
        Spacer(modifier = Modifier.height(24.dp))
        Avatar(avatar, size = 120.dp, onAvatarClick = { launchPhotoPicker() })
        Spacer(modifier = Modifier.height(24.dp))
        SettingItem(
            iconRes = R.drawable.ic_account_setting,
            titleRes = R.string.account_settings,
            isNext = true
        )
        SettingItem(
            iconRes = R.drawable.ic_app_theme,
            titleRes = R.string.app_theme,
            isNext = true
        )
        SettingItem(
            iconRes = R.drawable.ic_notifications,
            titleRes = R.string.notifications,
            isNext = true
        )
        SettingItem(
            iconRes = R.drawable.ic_report_problem,
            titleRes = R.string.report_problem,
            isNext = true
        )
        SettingItem(
            iconRes = R.drawable.ic_help,
            titleRes = R.string.help,
            isNext = true
        )
        Spacer(modifier = Modifier.height(56.dp))
        SettingItem(
            iconRes = R.drawable.ic_delete_account,
            titleRes = R.string.delete_account,
            isNext = false,
            tint = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun SettingItem(
    iconRes: Int,
    titleRes: Int,
    isNext: Boolean,
    modifier: Modifier = Modifier,
    tint: Color = Color.Black
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null, tint = tint
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = stringResource(id = titleRes),
            color = tint
        )
        if (isNext) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_navigate_next),
                contentDescription = null,
                tint = tint
            )
        }
    }
}