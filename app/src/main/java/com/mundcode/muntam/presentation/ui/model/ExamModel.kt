package com.mundcode.muntam.presentation.ui.model

import com.mundcode.domain.model.Exam
import com.mundcode.domain.model.enums.ExamState
import kotlinx.datetime.Instant

data class ExamModel( // todo 수정
    var id: Int = 0,
    val subjectId: Int,
    val name: String,
    val isFavorite: Boolean = false,
    val createdAt: Instant,
    val endAt: Instant? = null, // 시험이 끝났을 때 시간
    val lastAt: Instant? = null, // 시험 종료 또는 중간에 나갈 때 마지막으로 기록된 시험진행 시간
    val lastQuestionNumber: Int? = null, // 시험 기록 중 마지막으로 푼 문제
    val deletedAt: Instant? = null, // 소프트 딜리트 용도
    val state: ExamState = ExamState.READY
)

fun Exam.asStateModel() = ExamModel( // todo 수정
    id = id,
    subjectId = subjectId,
    name = name,
    isFavorite = isFavorite,
    createdAt = createdAt,
    endAt = endAt,
    lastAt = lastAt,
    lastQuestionNumber = lastQuestionNumber,
    deletedAt = deletedAt,
    state = state
)

fun ExamModel.asExternalModel(): Exam = Exam( // todo 수정
    id = id,
    subjectId = subjectId,
    name = name,
    isFavorite = isFavorite,
    createdAt = createdAt,
    endAt = endAt,
    lastAt = lastAt,
    lastQuestionNumber = lastQuestionNumber,
    deletedAt = deletedAt,
    state = state
)
