package com.rocky.whisper.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser
import com.rocky.whisper.R
import com.rocky.whisper.WhisperViewModel
import com.rocky.whisper.util.Avatar
import com.rocky.whisper.util.WhisperTopAppBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    whisperViewModel: WhisperViewModel,
    viewModel: HomeViewModel
) {
    LaunchedEffect(true) {
        whisperViewModel.signInAnonymously()
    }
    HomeContent(user = whisperViewModel.userState.value)
}

@Composable
fun HomeContent(modifier: Modifier = Modifier, user: FirebaseUser?) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp)
    ) {
        WhisperTopAppBar(R.string.home, true)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
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
            item {
                Text(
                    text = stringResource(id = R.string.recent_chats),
                    fontWeight = FontWeight.Bold
                )
            }
            items(10) {
                WhisperItem()
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
            text = stringResource(id = R.string.unread_message),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
@Preview(showBackground = true)
fun WhisperItem(modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Avatar(size = 64.dp)
        Spacer(modifier = Modifier.width(4.dp))
        Column(
            Modifier
                .fillMaxHeight()
                .padding(top = 8.dp, bottom = 12.dp)
        ) {
            Text(text = "name")
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "last message...",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 12.dp), Alignment.BottomEnd
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check_circle),
                contentDescription = null,
                Modifier.size(16.dp)
            )
        }
    }
}