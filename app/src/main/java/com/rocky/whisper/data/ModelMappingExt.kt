package com.rocky.whisper.data

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