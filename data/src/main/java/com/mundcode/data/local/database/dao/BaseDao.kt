package com.mundcode.data.local.database.dao

import androidx.room.Insert
import androidx.room.Query

interface BaseDao<T> {
    @Insert
    fun insert(vararg  obj: T)
}