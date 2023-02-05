package com.mundcode.muntam.presentation.ui.main.subjects

import androidx.compose.ui.graphics.Color
import com.mundcode.domain.model.Subject
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class SubjectState(
    val id: Int = 0,
    val subjectTitle: String,
    val backgroundColor: Color = Color.DarkGray,
    val lastExamDate: String = "22.22.22",
    val pinned: Boolean = false
)

fun Subject.asStateModel() = SubjectState( // todo
    id = id,
    subjectTitle = name,
    lastExamDate = "2022.12.12"
)

fun SubjectState.asExternalModel(): Subject = Subject( // todo
    id = id,
    name = subjectTitle,
    totalQuestionNumber = 1,
    timeLimit = 100000L,
    createdAt = Clock.System.now(),
)
