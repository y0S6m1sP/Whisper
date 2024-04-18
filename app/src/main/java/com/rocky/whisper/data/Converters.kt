package com.rocky.whisper.data

import androidx.room.TypeConverter
import com.rocky.whisper.data.user.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromListString(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toListString(listString: String): List<String> {
        return Json.decodeFromString(listString)
    }

    @TypeConverter
    fun fromUserList(userList: List<User>): String {
        return Json.encodeToString(userList)
    }

    @TypeConverter
    fun toUserList(userListString: String): List<User> {
        return Json.decodeFromString(userListString)
    }
}