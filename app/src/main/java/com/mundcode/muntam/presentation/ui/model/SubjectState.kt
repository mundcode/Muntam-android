package com.mundcode.muntam.presentation.ui.model

import androidx.compose.ui.graphics.Color
import com.mundcode.domain.model.Subject
import kotlinx.datetime.Clock

data class SubjectState( // todo 수정
    val id: Int = 0,
    val subjectTitle: String,
    val backgroundColor: Color = Color.DarkGray,
    val lastExamDate: String = "22.22.22",
    val pinned: Boolean = false
)

fun Subject.asStateModel() = SubjectState( // todo 수정
    id = id,
    subjectTitle = name,
    lastExamDate = "2022.12.12"
)

fun SubjectState.asExternalModel(): Subject = Subject( // todo 수정
    id = id,
    name = subjectTitle,
    totalQuestionNumber = 1,
    timeLimit = 100000L,
    createdAt = Clock.System.now()
)
