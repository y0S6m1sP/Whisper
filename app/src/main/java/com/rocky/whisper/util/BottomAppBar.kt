package com.rocky.whisper.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rocky.whisper.R
import com.rocky.whisper.WhisperNavigationActions
import com.rocky.whisper.ui.theme.IconStyle

enum class BottomAppBarTab {
    Home, Setting
}

@Composable
fun WhisperBottomAppBar(
    modifier: Modifier = Modifier,
    navigationActions: WhisperNavigationActions,
    selectedTab: BottomAppBarTab,
    content: @Composable () -> Unit
) {
    Column(modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            content()
        }
        BottomAppBar(
            navigateToHome = { navigationActions.navigateToHome() },
            navigateToSetting = { navigationActions.navigateToSetting() },
            selectedTab = selectedTab
        )
    }
}

@Composable
fun BottomAppBar(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToSetting: () -> Unit,
    selectedTab: BottomAppBarTab
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(64.dp),
        Arrangement.SpaceAround,
        Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .noRippleClickable { navigateToHome() },
            Alignment.Center
        ) {
            WhisperIconButton(
                painter = painterResource(id = R.drawable.ic_outlined_home),
                selectedPainter = painterResource(id = R.drawable.ic_filled_home),
                contentDescription = "home",
                isSelect = selectedTab == BottomAppBarTab.Home
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .noRippleClickable { navigateToSetting() },
            Alignment.Center
        ) {
            WhisperIconButton(
                painter = painterResource(id = R.drawable.ic_outlined_setting),
                selectedPainter = painterResource(id = R.drawable.ic_filled_setting),
                contentDescription = "setting",
                isSelect = selectedTab == BottomAppBarTab.Setting
            )
        }
    }
}

@Composable
fun WhisperIconButton(
    painter: Painter,
    selectedPainter: Painter,
    contentDescription: String?,
    isSelect: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .width(IconStyle.width)
            .height(IconStyle.height),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Icon(
            painter = if (isSelect) selectedPainter else painter,
            contentDescription = contentDescription,
            tint = if (isSelect) MaterialTheme.colorScheme.primary else Color.Black
        )
        Spacer(modifier = Modifier.height(IconStyle.iconLineSpace))
        if (isSelect) {
            AnimateIconBottomLine()
        }
    }
}

@Composable
fun AnimateIconBottomLine(modifier: Modifier = Modifier) {
    val lineColor = MaterialTheme.colorScheme.primary
    val progress = remember { Animatable(0f) }
    LaunchedEffect(progress) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
    }
    Canvas(modifier = modifier.fillMaxWidth()) {
        val center = size.width / 2
        drawLine(
            start = Offset(x = center, y = 0f),
            end = Offset(
                x = center - (IconStyle.iconLineLength.toPx() * progress.value),
                y = 0f
            ),
            color = lineColor,
            strokeWidth = IconStyle.iconLineStrokeWidth.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            start = Offset(x = center, y = 0f),
            end = Offset(
                x = center + (IconStyle.iconLineLength.toPx() * progress.value),
                y = 0f
            ),
            color = lineColor,
            strokeWidth = IconStyle.iconLineStrokeWidth.toPx(),
            cap = StrokeCap.Round
        )
    }
}
