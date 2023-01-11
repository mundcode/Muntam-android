package com.mundcode.domain.model

data class Subject(
    val name: String,
    val totalQuestionNumber: Int,
    val timeLimit: Long,
    val createdAt: Long
)
