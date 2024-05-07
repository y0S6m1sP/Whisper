package com.rocky.whisper.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rocky.whisper.R
import com.rocky.whisper.core.component.Avatar
import com.rocky.whisper.data.chat.Message
import com.rocky.whisper.data.chat.isCurrentUser
import com.rocky.whisper.data.chat.isImage
import com.rocky.whisper.data.chat.isImageLoading
import com.rocky.whisper.feature.chat.util.convertLocalDateToDateString
import com.rocky.whisper.feature.chat.util.convertTimestampToTime
import java.time.LocalDate

@Composable
fun MessageHeader(date: LocalDate) {
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
fun MessageItem(
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
            when {
                message.isImageLoading() -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_image),
                        contentDescription = "loading"
                    )
                }

                message.isImage() -> {
                    AsyncImage(
                        modifier = modifier.clip(
                            RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp,
                                bottomStart = 20.dp
                            )
                        ),
                        model = message.image,
                        placeholder = painterResource(id = R.drawable.ic_image),
                        contentDescription = "image"
                    )
                }

                else -> {
                    Text(text = message.message)
                }
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
            when {
                message.isImageLoading() -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_image),
                        contentDescription = "loading"
                    )
                }

                message.isImage() -> {
                    OtherUserImageItem(uri = message.image)
                }

                else -> {
                    Text(text = message.message)
                }
            }
        }
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = convertTimestampToTime(message.lastUpdate),
            maxLines = 1
        )
    }
}

@Composable
private fun OtherUserImageItem(modifier: Modifier = Modifier, uri: String) {
    AsyncImage(
        modifier = modifier.clip(
            RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomEnd = 20.dp
            )
        ),
        model = uri,
        contentDescription = "image"
    )
}