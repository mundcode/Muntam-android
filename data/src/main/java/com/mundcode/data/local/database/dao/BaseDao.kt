package com.mundcode.data.local.database.dao

import androidx.room.Insert

interface BaseDao<T> {
    @Insert
    suspend fun insertAll(entities: List<T>)

    @Insert
    suspend fun insert(entity: T): Long
}
