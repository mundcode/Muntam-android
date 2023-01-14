package com.mundcode.domain.model

import kotlinx.datetime.Instant

data class Exam(
    var id: Int,
    val subjectId: Int,
    val name: String,
    val isFavorite: Boolean,
    val createdAt: Instant,
    val endAt: Instant?,
    val modifiedAt: Instant?,
    val deletedAt: Instant?,
    val state: ExamState
)

