package com.mundcode.data.local.database.dao

import androidx.room.Insert

interface BaseDao<T> {
    @Insert
    suspend fun insert(vararg obj: List<T>)
}