package com.mundcode.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mundcode.domain.model.Subject
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
    val modifiedAt: Instant? = null,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Instant? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun Subject.asExternalModel(): Subject = Subject(
    id = id,
    name = name,
    totalQuestionNumber = totalQuestionNumber,
    timeLimit = timeLimit,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
    deletedAt = deletedAt
)
