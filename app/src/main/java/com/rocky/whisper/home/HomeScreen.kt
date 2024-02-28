package com.rocky.whisper.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rocky.whisper.R
import com.rocky.whisper.util.WhisperTopAppBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    HomeContent()
}

@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp)
    ) {
        WhisperTopAppBar(R.string.home, true)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            UnReadWhisper()
            Spacer(modifier = Modifier.weight(1f))
            ElevatedButton(onClick = { /*TODO*/ }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_whisper),
                        contentDescription = "whisper"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.whisper), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun UnReadWhisper(modifier: Modifier = Modifier) {
    Column {
        Text(text = "23", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text(
            text = stringResource(id = R.string.unread_whisper),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray
        )
    }
}