@file:OptIn(FlowPreview::class)

package com.rocky.whisper.feature.chat

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.rocky.whisper.R
import com.rocky.whisper.core.component.Avatar
import com.rocky.whisper.core.component.DefaultTopAppBar
import com.rocky.whisper.core.util.bitmapToByteArray
import com.rocky.whisper.core.util.noRippleClickable
import com.rocky.whisper.data.chat.Message
import com.rocky.whisper.feature.chat.util.convertLocalDateToDateString
import com.rocky.whisper.feature.chat.util.convertTimestampToTime
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import java.time.LocalDate

@Composable
fun ChatScreen(
    oppositeUserAvatar: String,
    topAppBarTitle: String,
    onBackPressed: () -> Unit,
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(key1 = Unit) {
        val listener = viewModel.fetchMessage()
        onDispose {
            listener.remove()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.observeMessage()
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->

        val bitmap =
            uri?.let { BitmapFactory.decodeStream(context.contentResolver.openInputStream(it)) }
        bitmap?.let { viewModel.sendImage(bitmapToByteArray(it)) }
    }

    fun launchPhotoPicker() {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    ChatContent(
        oppositeUserAvatar = oppositeUserAvatar,
        messageList = uiState.messageList,
        firstVisibleIndex = viewModel.firstVisibleIndex,
        topAppBarTitle = topAppBarTitle,
        onBackPressed = { onBackPressed() },
        onSendMessage = { viewModel.sendMessage(it) },
        onSendImage = { launchPhotoPicker() },
        onFirstVisibleIndexChange = { viewModel.updateFirstVisibleIndex(it) },
        modifier = modifier
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChatContent(
    oppositeUserAvatar: String,
    messageList: Map<LocalDate, List<Message>>,
    firstVisibleIndex: String,
    topAppBarTitle: String,
    onBackPressed: () -> Unit,
    onSendMessage: (String) -> Unit,
    onSendImage: () -> Unit,
    onFirstVisibleIndexChange: (Int) -> Unit,
    modifier: Modifier,
) {
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = firstVisibleIndex.toInt()
    )

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }
            .debounce(500)
            .collectLatest { index ->
                onFirstVisibleIndexChange(index)
            }
    }

    Column(
        modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        DefaultTopAppBar(title = topAppBarTitle, onBackPressed = { onBackPressed() })
        Column(
            modifier = Modifier
                .weight(1f)
                .noRippleClickable { focusManager.clearFocus() },
            verticalArrangement = Arrangement.Bottom
        ) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                messageList.forEach { (localDate, messages) ->
                    stickyHeader {
                        MessageHeader(localDate)
                    }
                    items(messages) {
                        MessageItem(it, oppositeUserAvatar)
                    }
                }
            }
        }
        TypingBar(onSendMessage = onSendMessage, onSendImage = onSendImage)
    }
}

@Composable
private fun TypingBar(
    onSendMessage: (String) -> Unit,
    onSendImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(TextFieldValue()) }
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        IconButton(onClick = {
            onSendImage()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_image),
                contentDescription = "select image"
            )
        }
        BasicTextField(
            value = text,
            onValueChange = { targetValue ->
                text = targetValue
            },
            modifier = Modifier
                .weight(1f)
                .border(BorderStroke(1.dp, LocalContentColor.current), RoundedCornerShape(28.dp)),
            interactionSource = interactionSource,
            decorationBox = @Composable { innerTextField ->
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(all = 16.dp),
                ) {
                    innerTextField()
                }
            },
            textStyle = TextStyle(color = LocalContentColor.current, fontSize = 16.sp),
            cursorBrush = SolidColor(LocalContentColor.current)
        )
        IconButton(onClick = {
            onSendMessage(text.text)
            text = TextFieldValue()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filled_send),
                contentDescription = "send"
            )
        }
    }
}

@Composable
private fun MessageHeader(date: LocalDate) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .padding(vertical = 4.dp, horizontal = 16.dp),
            text = convertLocalDateToDateString(date),
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

@Composable
private fun MessageItem(
    message: Message,
    oppositeUserAvatar: String,
    modifier: Modifier = Modifier
) {
    when {
        message.isCurrentUser() -> {
            CurrentUserMessageItem(modifier = modifier, message = message)
        }

        else -> {
            OtherUserMessageItem(
                modifier = modifier,
                message = message,
                oppositeUserAvatar = oppositeUserAvatar
            )
        }
    }
}

@Composable
private fun CurrentUserMessageItem(modifier: Modifier = Modifier, message: Message) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = convertTimestampToTime(message.lastUpdate)
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = 28.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(16.dp)
                .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.5f)
        ) {
            if (message.isImageUrl()) {
                AsyncImage(
                    modifier = modifier.clip(
                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp)
                    ),
                    model = message.message,
                    contentDescription = "image"
                )
            } else {
                Text(text = message.message)
            }
        }
    }
}

@Composable
private fun OtherUserMessageItem(
    modifier: Modifier = Modifier,
    message: Message,
    oppositeUserAvatar: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Avatar(uri = oppositeUserAvatar, size = 32.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            Modifier
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomEnd = 28.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(16.dp)
                .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.5f)
        ) {
            if (message.isImageUrl()) {
                AsyncImage(
                    modifier = modifier.clip(
                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp)
                    ),
                    model = message.message,
                    contentDescription = "image"
                )
            } else {
                Text(text = message.message)
            }
        }
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = convertTimestampToTime(message.lastUpdate),
            maxLines = 1
        )
    }
}