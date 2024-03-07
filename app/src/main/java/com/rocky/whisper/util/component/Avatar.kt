package com.rocky.whisper.util.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.rocky.whisper.util.noRippleClickable

@Composable
fun Avatar(uri: String = "", size: Dp, onAvatarClick: (() -> Unit)? = null) {
    AsyncImage(
        model = uri,
        contentDescription = null,
        error = rememberVectorPainter(image = Icons.Sharp.AccountCircle),
        placeholder = rememberVectorPainter(image = Icons.Sharp.AccountCircle),
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .noRippleClickable {
                onAvatarClick?.invoke()
            },
        contentScale = ContentScale.Crop
    )
}

@Composable
fun CropAvatarDim(padding: Dp, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val bounds = Rect(center, size.minDimension / 2 - padding.toPx())
        val circlePath = Path().apply {
            addOval(bounds)
        }
        clipPath(circlePath, clipOp = ClipOp.Difference) {
            drawRect(SolidColor(Color.Black.copy(alpha = 0.5f)))
        }
    }
}