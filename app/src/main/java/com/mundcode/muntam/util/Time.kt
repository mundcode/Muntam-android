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
