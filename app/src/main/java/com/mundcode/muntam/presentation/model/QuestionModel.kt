package com.mundcode.muntam.presentation.model

import com.mundcode.domain.model.Question
import com.mundcode.domain.model.enums.QuestionState
import com.mundcode.muntam.util.asCurrentTimerText
import com.mundcode.muntam.util.asLapsedTimeText
import kotlinx.datetime.Instant

data class QuestionModel(
    val id: Int = 0,
    val subjectId: Int,
    val examId: Int,
    val questionNumber: Int,
    val isCorrect: Boolean = true,
    val isAlarm: Boolean = false,
    val isFavorite: Boolean = false,
    val lapsedTime: Long = 0,
    val lapsedExamTime: Long = 0,
    val createdAt: Instant,
    val modifiedAt: Instant? = null,
    val deletedAt: Instant? = null,
    val state: QuestionState = QuestionState.READY
) {
    val lapsedExamTimeText = lapsedExamTime.asCurrentTimerText()
    val lapsedTimeText = lapsedTime.asLapsedTimeText()
}

fun QuestionModel.asExternalModel() = Question(
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

fun Question.asStateModel() = QuestionModel(
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
