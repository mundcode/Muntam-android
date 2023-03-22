package com.mundcode.domain.model

import com.mundcode.domain.model.enums.QuestionState
import kotlinx.datetime.Instant

data class Question(
    val id: Int = 0,
    val subjectId: Int,
    val examId: Int,
    val questionNumber: Int,
    val isCorrect: Boolean = false,
    val isFavorite: Boolean = false,
    val isAlarm: Boolean = false,
    val lapsedTime: Long = 0,
    val lapsedExamTime: Long = 0,
    val createdAt: Instant,
    val modifiedAt: Instant? = null,
    val deletedAt: Instant? = null,
    val state: QuestionState = QuestionState.READY
)
