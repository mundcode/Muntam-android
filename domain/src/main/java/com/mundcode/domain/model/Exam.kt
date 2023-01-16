package com.mundcode.domain.model

import kotlinx.datetime.Instant

data class Exam(
    var id: Int,
    val subjectId: Int,
    val name: String,
    val isFavorite: Boolean,
    val createdAt: Instant,
    val endAt: Instant? = null,
    val modifiedAt: Instant? = null,
    val deletedAt: Instant? = null,
    val state: ExamState
)
