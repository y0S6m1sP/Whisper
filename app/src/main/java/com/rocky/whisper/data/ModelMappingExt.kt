package com.rocky.whisper.data

import com.rocky.whisper.data.source.local.LocalChatroom
import com.rocky.whisper.data.source.local.LocalMessage
import com.rocky.whisper.data.source.local.LocalUser

fun User.toLocal() = LocalUser(
    id = id!!,
    name = name!!,
    avatar = avatar!!
)

fun LocalUser.toExternal() = User(
    id = id,
    name = name,
    avatar = avatar
)

fun Chatroom.toLocal() = LocalChatroom(
    id = id!!,
    users = users!!,
    userDetails = userDetails!!,
    lastMessage = lastMessage!!,
    lastUpdate = lastUpdate!!,
)

fun LocalChatroom.toExternal() = Chatroom(
    id = id,
    users = users,
    userDetails = userDetails,
    lastMessage = lastMessage,
    lastUpdate = lastUpdate,
)

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