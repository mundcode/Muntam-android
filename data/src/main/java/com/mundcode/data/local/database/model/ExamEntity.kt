package com.mundcode.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mundcode.domain.model.Exam
import com.mundcode.domain.model.enums.ExamState
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = "exams",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subject_id"]
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
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "time_limit")
    val timeLimit: Long,
    @ColumnInfo(name = "complete_ad")
    val completeAd: Boolean = false,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "end_at")
    val endAt: Instant? = null,
    @ColumnInfo(name = "modified_at")
    val lastAt: Long? = null,
    @ColumnInfo(name = "last_question_number")
    val lastQuestionNumber: Int? = null,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Instant? = null,
    val state: ExamState = ExamState.READY
)

fun ExamEntity.asExternalModel(): Exam = Exam(
    id = id,
    subjectId = subjectId,
    name = name,
    isFavorite = isFavorite,
    timeLimit = timeLimit,
    completeAd = completeAd,
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
    timeLimit = timeLimit,
    completeAd = completeAd,
    createdAt = createdAt,
    endAt = endAt,
    lastAt = lastAt,
    lastQuestionNumber = lastQuestionNumber,
    deletedAt = deletedAt,
    state = state
)

fun Exam.asEntityWithModify(): ExamEntity = ExamEntity(
    id = id,
    subjectId = subjectId,
    name = name,
    isFavorite = isFavorite,
    timeLimit = timeLimit,
    completeAd = completeAd,
    createdAt = createdAt,
    endAt = endAt,
    lastAt = lastAt,
    lastQuestionNumber = lastQuestionNumber,
    deletedAt = deletedAt,
    state = state
)


fun createExamEntities(
    size: Int,
    subjectId: Int = 0
) = (1..size).map {
    createExamEntity(
        id = it,
        subjectId = subjectId,
    )
}

fun createExamEntity(
    id: Int,
    subjectId: Int
) = ExamEntity(
    id = id,
    subjectId = subjectId,
    name = "테스트 시험 이름 : $id",
    createdAt = Clock.System.now(),
    timeLimit = id * 100000L
)
