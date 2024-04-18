package com.rocky.whisper.feature.chat.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

fun convertTimestampToTime(timestamp: Long): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(date)
}

fun convertLocalDateToDateString(localDate: LocalDate): String {
    val date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
    val sdf = SimpleDateFormat("MM/dd", Locale.getDefault())
    return sdf.format(date)
}