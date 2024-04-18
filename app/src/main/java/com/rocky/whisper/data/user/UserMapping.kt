package com.rocky.whisper.data.user

import com.rocky.whisper.data.user.local.LocalUser

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