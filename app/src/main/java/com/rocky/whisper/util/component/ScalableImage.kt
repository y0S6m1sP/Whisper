package com.rocky.whisper.util.component

import android.net.Uri
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ScalableImage(uri: String, modifier: Modifier = Modifier) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var imageHeight by remember { mutableStateOf(0.dp) }
    var imageWidth by remember { mutableStateOf(0.dp) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = Uri.parse(uri),
            contentDescription = null,
            modifier = Modifier
                .onGloballyPositioned {
                    imageWidth = it.size.width.dp
                    imageHeight = it.size.height.dp
                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale *= zoom

                        scale = scale.coerceIn(1f, 3f)

                        val maxOffsetX = (imageWidth * scale) / 2 - (imageWidth / 2)
                        val maxOffsetY = maxOffsetX + ((imageHeight - imageWidth) / 2 * scale)

                        val newOffsetX = if (scale == 1f) {
                            0f
                        } else {
                            (offset.x + pan.x).coerceIn(-maxOffsetX.value, maxOffsetX.value)
                        }
                        val newOffsetY =
                            (offset.y + pan.y).coerceIn(-maxOffsetY.value, maxOffsetY.value)

                        offset = Offset(newOffsetX, newOffsetY)
                    }
                }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        )
    }
}