package com.mundcode.muntam.data.local.database.entity

data class QuestionEntity(
    val subjectId: Int,
    val examId: Int,
    val questionId: Int,
    val name: String,
    val correct: Boolean,
}