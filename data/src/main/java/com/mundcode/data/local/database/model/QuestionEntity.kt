package com.mundcode.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mundcode.domain.model.Question
import com.mundcode.domain.model.enums.QuestionState
import kotlinx.datetime.Clock
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
    val isCorrect: Boolean = true,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "is_alarm")
    val isAlarm: Boolean = false,
    @ColumnInfo(name = "lapsed_time")
    val lapsedTime: Long = 0L,
    @ColumnInfo(name = "lapsed_exam_time")
    val lapsedExamTime: Long = 0L,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "modified_at")
    val modifiedAt: Instant? = null,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Instant? = null,
    val state: QuestionState = QuestionState.READY
)

fun QuestionEntity.asExternalModel() = Question(
    id = id,
    subjectId = subjectId,
    examId = examId,
    questionNumber = questionNumber,
    isCorrect = isCorrect,
    isFavorite = isFavorite,
    isAlarm = isAlarm,
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
    isAlarm = isAlarm,
    lapsedTime = lapsedTime,
    lapsedExamTime = lapsedExamTime,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
    deletedAt = deletedAt,
    state = state
)

fun Question.asEntityWithModify() = QuestionEntity(
    id = id,
    subjectId = subjectId,
    examId = examId,
    questionNumber = questionNumber,
    isCorrect = isCorrect,
    isFavorite = isFavorite,
    isAlarm = isAlarm,
    lapsedTime = lapsedTime,
    lapsedExamTime = lapsedExamTime,
    createdAt = createdAt,
    modifiedAt = Clock.System.now(),
    deletedAt = deletedAt,
    state = state
)

fun createQuestionEntities(size: Int, subjectId: Int, examId: Int) =
    (1..size).map {
        createQuestionEntity(
            id = it,
            subjectId = subjectId,
            examId = examId
        )
    }

fun createQuestionEntity(
    id: Int,
    subjectId: Int,
    examId: Int
) = QuestionEntity(
    id = id,
    subjectId = subjectId,
    examId = examId,
    // questionNumber 로 조회하는 기능을 따로 테스트 하기위해 + 1 추가
    questionNumber = id + 1,
    createdAt = Clock.System.now()
)
