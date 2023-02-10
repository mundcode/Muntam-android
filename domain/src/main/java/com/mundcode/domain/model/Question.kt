package com.mundcode.domain.model

import com.mundcode.domain.model.enums.QuestionState
import kotlinx.datetime.Instant

data class Question(
    val id: Int = 0,
    val subjectId: Int,
    val examId: Int,
    val name: String,
    val correct: Boolean,
    val lapsedTime: Long,
    val expiredTime: Long,
    val modifiedAt: Instant,
    val deletedAt: Instant,
    val state: QuestionState
)