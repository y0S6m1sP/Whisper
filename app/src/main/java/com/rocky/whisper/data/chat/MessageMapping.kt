package com.rocky.whisper.data.chat

import com.rocky.whisper.data.chat.local.LocalMessage

fun Message.toLocal(roomId: String) = LocalMessage(
    id = id,
    roomId = roomId,
    senderId = senderId,
    message = message,
    lastUpdate = lastUpdate,
)

fun LocalMessage.toExternal() = Message(
    id = id,
    senderId = senderId,
    message = message,
    lastUpdate = lastUpdate,
)