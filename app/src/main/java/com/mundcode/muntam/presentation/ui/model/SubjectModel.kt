package com.mundcode.muntam.presentation.ui.model

import com.mundcode.domain.model.Subject
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class SubjectModel( // todo 수정
    val id: Int = 0,
    val subjectTitle: String,
    val imoji: String,
    val lastExamName: String?,
    val lastExamDate: Instant?,
    val timeLimit: Long,
    val totalQuestionNumber: Int,
    val isPinned: Boolean = false
)

fun Subject.asStateModel() = SubjectModel( // todo 수정
    id = id,
    subjectTitle = name,
    imoji = imoji,
    lastExamName = lastExamName,
    lastExamDate = lastExamDate,
    timeLimit = timeLimit,
    totalQuestionNumber = totalQuestionNumber,
    isPinned = isPinnded
)

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
