package com.mundcode.domain.model

import kotlinx.datetime.Instant

data class Subject(
    val id: Int,
    val name: String,
    val totalQuestionNumber: Int,
    val timeLimit: Long,
    val createdAt: Instant,
    val modifiedAt: Instant?,
    val deletedAt: Instant?
)
