package com.rocky.whisper.util

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocky.whisper.R

@Composable
fun LogoTopAppBar(@StringRes titleRes: Int, isAvatar: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Face, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = titleRes),
            fontWeight = FontWeight.Bold
        )
        if (isAvatar) {
            Spacer(modifier = Modifier.weight(1f))
            Avatar(size = 32.dp)
        }
    }
}

@Composable
fun DefaultTopAppBar(
    title: String,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onBackPressed() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "back"
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )
    }
}