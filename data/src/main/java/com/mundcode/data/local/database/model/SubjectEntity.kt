package com.mundcode.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mundcode.domain.model.Subject
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name: String,
    val imoji: String,
    @ColumnInfo(name = "last_exam_name")
    val lastExamName: String?,
    @ColumnInfo(name = "last_exam_date")
    val lastExamDate: Instant?,
    @ColumnInfo(name = "is_pinned")
    val isPinned: Boolean,
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
)

fun SubjectEntity.asExternalModel(): Subject = Subject(
    id = id,
    name = name,
    imoji = imoji,
    lastExamName = lastExamName,
    lastExamDate = lastExamDate,
    totalQuestionNumber = totalQuestionNumber,
    timeLimit = timeLimit,
    isPinnded = isPinned,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
    deletedAt = deletedAt
)

fun Subject.asEntity(): SubjectEntity = SubjectEntity(
    id = id, // 업데이트 시 필요
    name = name,
    imoji = imoji,
    lastExamName = lastExamName,
    lastExamDate = lastExamDate,
    totalQuestionNumber = totalQuestionNumber,
    timeLimit = timeLimit,
    isPinned = isPinnded,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
    deletedAt = deletedAt
)

fun createSubjectEntities(size: Int) = (1..size).map {
    createSubjectEntity(id = it)
}

fun createSubjectEntity(id: Int): SubjectEntity {
    return SubjectEntity(
        id = id,
        name = "테스트 과목 : $id",
        imoji = "💪",
        lastExamName = "마지막 시험 이름 : $id",
        lastExamDate = Instant.fromEpochMilliseconds(1677842874000),
        totalQuestionNumber = id,
        timeLimit = 100000L,
        isPinned = id % 2 == 0,
        createdAt = Clock.System.now()
    )
}
