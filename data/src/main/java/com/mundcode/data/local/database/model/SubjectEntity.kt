package com.mundcode.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    @ColumnInfo(name = "total_question_number")
    val totalQuestionNumber: Int,
    @ColumnInfo(name = "time_limit")
    val timeLimit: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)