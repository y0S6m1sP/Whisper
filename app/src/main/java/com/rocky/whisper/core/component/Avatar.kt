package com.rocky.whisper.core.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.rocky.whisper.R
import com.rocky.whisper.core.util.noRippleClickable

@Composable
fun Avatar(uri: String = "", size: Dp, onAvatarClick: (() -> Unit)? = null) {
    AsyncImage(
        model = uri,
        contentDescription = null,
        error = painterResource(id = R.drawable.img_default_avatar),
        placeholder = painterResource(id = R.drawable.img_default_avatar),
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .noRippleClickable {
                onAvatarClick?.invoke()
            },
        contentScale = ContentScale.Crop
    )
}