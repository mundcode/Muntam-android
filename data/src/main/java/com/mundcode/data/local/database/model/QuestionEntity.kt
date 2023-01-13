package com.mundcode.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subject_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExamEntity::class,
            parentColumns = ["id"],
            childColumns = ["exam_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QuestionEntity(
    @ColumnInfo(name = "subject_id")
    val subjectId: Int,
    @ColumnInfo(name = "exam_id")
    val examId: Int,
    val name: String,
    val correct: Boolean,
    @ColumnInfo(name = "lapsed_time")
    val lapsedTime: Long,
    @ColumnInfo(name = "lapsed_exam_time")
    val lapsedExamTime: Long,
    @ColumnInfo(name = "expired_time")
    val expiredTime: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "modified_at")
    val modifiedAt: Long?,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long?
) {
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
}
