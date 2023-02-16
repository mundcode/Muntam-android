package com.mundcode.muntam.presentation.ui.model

import com.mundcode.domain.model.Question
import com.mundcode.domain.model.enums.QuestionState
import kotlinx.datetime.Instant

data class QuestionState(
    val id: Int = 0,
    val subjectId: Int,
    val examId: Int,
    val questionNumber: Int,
    val isCorrect: Boolean = false,
    val isFavorite: Boolean = false,
    val lapsedTime: Long = 0,
    val lapsedExamTime: Long = 0,
    val createdAt: Instant,
    val modifiedAt: Instant? = null,
    val deletedAt: Instant? = null,
    val state: QuestionState = QuestionState.READY
)

fun com.mundcode.muntam.presentation.ui.model.QuestionState.asExternalModel() = Question(
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

fun Question.asStateModel() = QuestionState(
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
