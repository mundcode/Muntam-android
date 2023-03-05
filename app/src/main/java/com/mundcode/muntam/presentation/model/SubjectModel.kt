package com.mundcode.muntam.presentation.model

import com.mundcode.domain.model.Subject
import com.mundcode.muntam.util.asMTDateText
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class SubjectModel( // todo 수정
    val id: Int = 0,
    val subjectTitle: String,
    val emoji: String,
    val timeLimit: Long,
    val totalQuestionNumber: Int,
    val lastExamName: String? = null,
    val lastExamDate: Instant? = null,
    val isPinned: Boolean = false
) {
    val lastExamDateText: String? =
        lastExamDate?.asMTDateText()
}

fun Subject.asStateModel(): SubjectModel {
    return SubjectModel( // todo 수정
        id = id,
        subjectTitle = name,
        emoji = emoji,
        lastExamName = lastExamName,
        lastExamDate = lastExamDate,
        timeLimit = timeLimit,
        totalQuestionNumber = totalQuestionNumber,
        isPinned = isPinnded
    )
}

fun SubjectModel.asExternalModel(): Subject = Subject( // todo 수정
    id = id,
    name = subjectTitle,
    emoji = emoji,
    lastExamName = lastExamName,
    lastExamDate = lastExamDate,
    totalQuestionNumber = totalQuestionNumber,
    timeLimit = timeLimit,
    isPinnded = isPinned,
    createdAt = Clock.System.now()
)

fun createMockedSubjectModels(size: Int): List<SubjectModel> {
    return (1..size).map {
        createMockedSubjectModel(it)
    }
}

fun createMockedSubjectModel(id: Int): SubjectModel {
    return SubjectModel(
        id = id,
        subjectTitle = "테스트 과목 : $id",
        emoji = "💪",
        lastExamName = "마지막 시험 이름 : $id",
        lastExamDate = Clock.System.now(),
        totalQuestionNumber = id,
        timeLimit = 100000L,
        isPinned = id % 2 == 0
    )
}
