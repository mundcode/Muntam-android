package com.mundcode.muntam.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// todo 테스트
fun Instant.asMTDateText(): String {
    val date = this.toLocalDateTime(TimeZone.UTC)
    return try {
        "%02d.%02d.%02d"
            .format("${date.year}".takeLast(2).toInt(), date.monthNumber, date.dayOfMonth)
    } catch (e: Exception) {
        "$e"
    }
}

fun Long.getTimeLimitText(): String {
    val totalSec = this / 1000
    val sec = totalSec % 60
    val totalMin = totalSec / 60
    val min = totalMin % 60
    val hour = totalMin / 60
    return "%d시간 %02d분 %02d초".format(hour, min, sec)
}
