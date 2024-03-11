package com.rocky.whisper.ui.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rocky.whisper.R
import com.rocky.whisper.data.Message
import com.rocky.whisper.util.component.Avatar
import com.rocky.whisper.util.component.DefaultTopAppBar
import com.rocky.whisper.util.noRippleClickable
import timber.log.Timber


@Composable
fun ChatScreen(
    oppositeUserAvatar: String,
    topAppBarTitle: String,
    onBackPressed: () -> Unit,
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchMessage()
        viewModel.observeMessage()
    }

    ChatContent(
        oppositeUserAvatar = oppositeUserAvatar,
        messageList = uiState.messageList,
        topAppBarTitle = topAppBarTitle,
        onBackPressed = { onBackPressed() },
        onSendMessage = { viewModel.sendMessage(it) },
        modifier = modifier
    )
}

@Composable
fun ChatContent(
    oppositeUserAvatar: String,
    messageList: List<Message>,
    topAppBarTitle: String,
    onBackPressed: () -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier.fillMaxHeight()
    ) {
        DefaultTopAppBar(title = topAppBarTitle, onBackPressed = { onBackPressed() })
        Column(
            modifier = Modifier
                .weight(1f)
                .noRippleClickable { focusManager.clearFocus() },
            verticalArrangement = Arrangement.Bottom
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true
            ) {
                items(messageList) {
                    MessageItem(it, oppositeUserAvatar)
                }
            }
        }
        TypingBar(onSendMessage = onSendMessage)
    }
}

@Composable
fun TypingBar(onSendMessage: (String) -> Unit, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf(TextFieldValue()) }
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {


        BasicTextField(
            value = text,
            onValueChange = { targetValue ->
                text = targetValue
            },
            modifier = Modifier
                .weight(1f)
                .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(28.dp)),
            interactionSource = interactionSource,
            decorationBox = @Composable { innerTextField ->
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(all = 16.dp),
                ) {
                    innerTextField()
                }
            }
        )
        IconButton(onClick = { onSendMessage(text.text) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filled_send),
                contentDescription = "send"
            )
        }
    }
}

@Composable
fun MessageItem(message: Message, oppositeUserAvatar: String, modifier: Modifier = Modifier) {
    if (message.isCurrentUser()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box(modifier = Modifier.fillMaxWidth(0.2f))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = 28.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(16.dp)
            ) {
                Text(text = message.message)
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth(0.8f)
                .padding(start = 12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Timber.e("oppositeUserAvatar: $oppositeUserAvatar")
            Avatar(uri = oppositeUserAvatar, size = 32.dp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message.message,
                Modifier
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomEnd = 28.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(16.dp)
            )
        }
    }
}