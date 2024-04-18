package com.rocky.whisper.data.home

import com.rocky.whisper.data.home.local.LocalChatroom

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
    firstVisibleIndex = firstVisibleIndex,
    lastUpdate = lastUpdate,
)