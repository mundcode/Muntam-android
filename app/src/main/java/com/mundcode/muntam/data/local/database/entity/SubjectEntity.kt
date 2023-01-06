package com.mundcode.muntam.data.local.database.entity

data class SubjectEntity(
    val subjectId: Int,
    val name: String,
    val totalQuestionNumber: Int,
    val timeLimit: Long
)