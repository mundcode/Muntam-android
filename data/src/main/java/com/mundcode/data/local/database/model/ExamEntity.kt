package com.mundcode.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mundcode.domain.model.Exam
import com.mundcode.domain.model.enums.ExamState
import kotlinx.datetime.Instant

@Entity(
    tableName = "exams",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subject_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class ExamEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "subject_id")
    val subjectId: Int,
    val name: String,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "end_at")
    val endAt: Instant?,
    @ColumnInfo(name = "modified_at")
    val lastAt: Instant? = null,
    @ColumnInfo(name = "last_question_number")
    val lastQuestionNumber: Int? = null,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Instant? = null,
    val state: ExamState
)

fun ExamEntity.asExternalModel(): Exam = Exam(
    id = id,
    subjectId = subjectId,
    name = name,
    isFavorite = isFavorite,
    createdAt = createdAt,
    endAt = endAt,
    lastAt = lastAt,
    lastQuestionNumber = lastQuestionNumber,
    deletedAt = deletedAt,
    state = state
)

fun Exam.asEntity(): ExamEntity = ExamEntity(
    id = id,
    subjectId = subjectId,
    name = name,
    isFavorite = isFavorite,
    createdAt = createdAt,
    endAt = endAt,
    lastAt = lastAt,
    lastQuestionNumber = lastQuestionNumber,
    deletedAt = deletedAt,
    state = state
)
