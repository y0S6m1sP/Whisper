package com.rocky.whisper.util.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rocky.whisper.R
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
fun CropAvatarDim(
    padding: Dp,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp,
    isLoading: Boolean = false,
    isSuccess: Boolean = false,
    isComplete: Boolean = false,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    onFinished: () -> Unit,
) {
    val angleRatio = remember {
        Animatable(0f)
    }
    var visible by remember {
        mutableStateOf(false)
    }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1.0f else 0f,
        label = "alpha",
        animationSpec = tween(durationMillis = 500),
        finishedListener = {
            onFinished.invoke()
        }
    )

    LaunchedEffect(key1 = isLoading, key2 = isComplete) {
        if (isLoading) {
            angleRatio.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 10000,
                    easing = LinearEasing
                )
            )
        }

        if (isComplete) {
            visible = true
            angleRatio.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearEasing
                )
            )
        }
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val imageRes = if (isSuccess) R.drawable.ic_check_circle else R.drawable.ic_error_circle
        val color = if (isSuccess) progressColor else MaterialTheme.colorScheme.error
        Image(
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer {
                    alpha = animatedAlpha
                },
            painter = painterResource(id = imageRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color)
        )
    }

    Canvas(modifier = modifier) {
        val bounds = Rect(center, size.minDimension / 2 - padding.toPx())
        val circlePath = Path().apply {
            addOval(bounds)
        }
        clipPath(circlePath, clipOp = ClipOp.Difference) {
            drawRect(SolidColor(Color.Black.copy(alpha = 0.5f)))
        }
        drawArc(
            color = progressColor,
            startAngle = 90f,
            sweepAngle = 360f * angleRatio.value,
            useCenter = false,
            topLeft = bounds.topLeft,
            size = bounds.size,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}