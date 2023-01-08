package com.mundcode.data.local.database.entity

data class QuestionEntity(
    val subjectId: Int,
    val examId: Int,
    val questionId: Int,
    val name: String,
    val correct: Boolean,
    val lapsedTime: Long,
    val lapsedExamTime: Long,
    val expiredTime: Long
)