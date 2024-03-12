package com.rocky.whisper.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rocky.whisper.R
import com.rocky.whisper.data.Chatroom
import com.rocky.whisper.data.User
import com.rocky.whisper.util.component.Avatar
import com.rocky.whisper.util.component.LogoTopAppBar
import com.rocky.whisper.util.component.WhisperDialog

@Composable
fun HomeScreen(
    onItemClick: (id: String, name: String, avatar: String) -> Unit,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.observeChatroom()
        viewModel.observeUser()
    }

    HomeContent(
        onWhisper = { viewModel.showWhisperDialog() },
        user = uiState.user,
        recentChatList = uiState.recentChatList,
        onItemClick = onItemClick,
        onWhisperDialogDismiss = { viewModel.hideWhisperDialog() },
        onWhisperDialogSubmit = { viewModel.createRoom(it) },
        isShowWhisperDialog = uiState.isShowWhisperDialog,
        modifier = modifier
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    user: User?,
    recentChatList: List<Chatroom>,
    onItemClick: (id: String, name: String, avatar: String) -> Unit,
    onWhisper: () -> Unit,
    onWhisperDialogDismiss: () -> Unit,
    onWhisperDialogSubmit: (id: String) -> Unit,
    isShowWhisperDialog: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(start = 24.dp, end = 24.dp)
    ) {
        LogoTopAppBar(
            title = user?.name ?: stringResource(id = R.string.home),
            avatar = user?.avatar
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    UnReadWhisper()
                    Spacer(modifier = Modifier.weight(1f))
                    ElevatedButton(onClick = { onWhisper() }) {
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
            items(recentChatList) {
                WhisperItem(chatroom = it, onItemClick = onItemClick)
            }
        }
    }
    WhisperDialog(
        showDialog = isShowWhisperDialog,
        onDismiss = { onWhisperDialogDismiss() },
        onSubmit = { onWhisperDialogSubmit(it) })
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
fun WhisperItem(
    chatroom: Chatroom,
    onItemClick: (id: String, name: String, avatar: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val oppositeUser = chatroom.parseOppositeUser()
    val oppositeUserName = oppositeUser?.name ?: "unknown"
    val oppositeUserAvatar = oppositeUser?.avatar ?: ""
    Row(
        modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable {
                onItemClick(
                    chatroom.id!!,
                    oppositeUserName,
                    oppositeUserAvatar
                )
            }
    ) {
        Avatar(oppositeUserAvatar, size = 64.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            Modifier
                .fillMaxHeight()
                .padding(bottom = 8.dp)
        ) {
            Text(text = oppositeUserName)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${chatroom.lastMessage}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 8.dp), Alignment.BottomEnd
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check_circle),
                contentDescription = null,
                Modifier.size(16.dp)
            )
        }
    }
}