package com.mundcode.data.local.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverters {
    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? =
        instant?.toEpochMilliseconds()
}
