package com.rocky.whisper.chat

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.rocky.whisper.R
import com.rocky.whisper.util.Avatar
import com.rocky.whisper.util.DefaultTopAppBar


@Composable
fun ChatScreen(
    topAppBarTitle: String,
    onBackPressed: () -> Unit,
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    ChatContent(
        topAppBarTitle = topAppBarTitle,
        onBackPressed = { onBackPressed() }
    )
}

@Composable
fun ChatContent(topAppBarTitle: String, onBackPressed: () -> Unit) {
    Column(
        Modifier
            .fillMaxHeight()
            .padding()
    ) {
        DefaultTopAppBar(title = topAppBarTitle, onBackPressed = { onBackPressed() })
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true
            ) {
                item {
                    OtherMessageItem()
                }
                item {
                    UserMessageItem()
                }
                items(2) {
                    OtherMessageItem()
                }
                items(2) {
                    UserMessageItem()
                }
                items(2) {
                    OtherMessageItem()
                }
            }
        }
        TypingBar(onSend = {})
    }
}

@Composable
fun TypingBar(onSend: (String) -> Unit, modifier: Modifier = Modifier) {
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
        IconButton(onClick = { onSend(text.text) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filled_send),
                contentDescription = "send"
            )
        }
    }
}

@Composable
fun UserMessageItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = 28.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.lorem_ipsum))
        }
    }
}

@Composable
fun OtherMessageItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Avatar(size = 32.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.lorem_ipsum),
            Modifier
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomEnd = 28.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(16.dp)
        )
    }
}