package com.mundcode.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mundcode.domain.model.Question
import com.mundcode.domain.model.enums.QuestionState
import kotlinx.datetime.Instant

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subject_id"]
        ),
        ForeignKey(
            entity = ExamEntity::class,
            parentColumns = ["id"],
            childColumns = ["exam_id"]
        )
    ]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "subject_id")
    val subjectId: Int,
    @ColumnInfo(name = "exam_id")
    val examId: Int,
    @ColumnInfo(name = "question_number")
    val questionNumber: Int,
    @ColumnInfo(name = "is_correct")
    val isCorrect: Boolean,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,
    @ColumnInfo(name = "lapsed_time")
    val lapsedTime: Long,
    @ColumnInfo(name = "lapsed_exam_time")
    val lapsedExamTime: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "modified_at")
    val modifiedAt: Instant? = null,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Instant? = null,
    val state: QuestionState
)

fun QuestionEntity.asExternalModel() = Question(
    id = id,
    subjectId = subjectId,
    examId = examId,
    questionNumber = questionNumber,
    isCorrect = isCorrect,
    isFavorite = isFavorite,
    lapsedTime = lapsedTime,
    lapsedExamTime = lapsedExamTime,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
    deletedAt = deletedAt,
    state = state
)

fun Question.asEntity() = QuestionEntity(
    id = id,
    subjectId = subjectId,
    examId = examId,
    questionNumber = questionNumber,
    isCorrect = isCorrect,
    isFavorite = isFavorite,
    lapsedTime = lapsedTime,
    lapsedExamTime = lapsedExamTime,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
    deletedAt = deletedAt,
    state = state
)
