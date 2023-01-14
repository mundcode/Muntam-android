package com.mundcode.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "subjects")
data class SubjectEntity(
    val name: String,
    @ColumnInfo(name = "total_question_number")
    val totalQuestionNumber: Int,
    @ColumnInfo(name = "time_limit")
    val timeLimit: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "modified_at")
    val modifiedAt: Instant?,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Instant?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
