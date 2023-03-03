package com.mundcode.muntam.presentation.ui.model

import com.mundcode.domain.model.Subject
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class SubjectModel( // todo 수정
    val id: Int = 0,
    val subjectTitle: String,
    val imoji: String,
    val timeLimit: Long,
    val totalQuestionNumber: Int,
    val lastExamName: String? = null,
    val lastExamDate: Instant? = null,
    val isPinned: Boolean = false
) {
    val lastExamDateText: String? =
        lastExamDate?.toMTDateText()

}

fun Subject.asStateModel(): SubjectModel {
    return SubjectModel( // todo 수정
        id = id,
        subjectTitle = name,
        imoji = imoji,
        lastExamName = lastExamName,
        lastExamDate = lastExamDate,
        timeLimit = timeLimit,
        totalQuestionNumber = totalQuestionNumber,
        isPinned = isPinnded
    )
}

// todo 테스트
fun Instant.toMTDateText(): String {
    val date = this.toLocalDateTime(TimeZone.UTC)
    return "%02d.%02d.%02d".format(date.year, date.month, date.dayOfMonth)
}

fun SubjectModel.asExternalModel(): Subject = Subject( // todo 수정
    id = id,
    name = subjectTitle,
    imoji = imoji,
    lastExamName = lastExamName,
    lastExamDate = lastExamDate,
    totalQuestionNumber = totalQuestionNumber,
    timeLimit = timeLimit,
    isPinnded = isPinned,
    createdAt = Clock.System.now()
)

fun createMockedSubjectModel(id: Int): SubjectModel {
    return SubjectModel(
        id = id,
        subjectTitle = "테스트 과목 : $id",
        imoji = "💪",
        lastExamName = "마지막 시험 이름 : $id",
        lastExamDate = Instant.fromEpochMilliseconds(1677842874000),
        totalQuestionNumber = id,
        timeLimit = 100000L,
        isPinned = id % 2 == 0
    )
}