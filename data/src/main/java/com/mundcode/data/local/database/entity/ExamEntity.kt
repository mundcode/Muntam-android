package com.mundcode.data.local.database.entity

import java.util.Date

data class ExamEntity(
    val subjectId: Int,
    val examId: Int,
    val name: String,
    val isFavorite: Boolean,
    val createdAt: Date
)