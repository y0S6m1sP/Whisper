package com.rocky.whisper.ui.uploadavatar

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rocky.whisper.R
import com.rocky.whisper.util.component.CropAvatarDim
import com.rocky.whisper.util.component.DefaultTopAppBar
import com.rocky.whisper.util.component.ScalableImage
import com.rocky.whisper.util.noRippleClickable

@Composable
fun UploadAvatarScreen(
    uri: String,
    onBackPressed: () -> Unit,
    viewModel: UploadAvatarViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val cropAvatarPadding = LocalDensity.current.run { 24.dp.toPx() }
    UploadAvatarContent(
        uri,
        onBackPressed = onBackPressed,
        onUploadClick = { view ->
            viewModel.cropAndUploadAvatar(view, cropAvatarPadding)
        },
        isUploading = uiState.isUploading,
        isUploadSuccess = uiState.isUploadSuccess,
        isUploadComplete = uiState.isUploadComplete
    )
}

@Composable
fun UploadAvatarContent(
    uri: String,
    onBackPressed: () -> Unit,
    onUploadClick: (view: View) -> Unit,
    isUploading: Boolean,
    isUploadSuccess: Boolean,
    isUploadComplete: Boolean,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        ScalableImage(
            uri = uri,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        )
        CropAvatarDim(
            24.dp,
            modifier = Modifier.fillMaxSize(),
            isLoading = isUploading,
            isSuccess = isUploadSuccess,
            isComplete = isUploadComplete
        ) {
            onBackPressed()
        }
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.End
        ) {
            val view = LocalView.current
            DefaultTopAppBar(onBackPressed = { onBackPressed() }, tint = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.upload),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(24.dp)
                    .noRippleClickable(enabled = !isUploading) {
                        onUploadClick(view)
                    }
            )
        }
    }
}