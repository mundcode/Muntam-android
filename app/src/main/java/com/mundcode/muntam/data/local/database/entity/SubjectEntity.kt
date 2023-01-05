package com.mundcode.muntam.data.local.database.entity

data class SubjectEntity(
    val subjectId: Int,
    val subjectName: String,
    val isPinned: Boolean,
    val totalQuestionNumber: Int,
    val backgroundColor: String,
    val textColor: String
)