package com.mundcode.muntam.presentation.model

import androidx.compose.ui.graphics.Color
import com.mundcode.designsystem.theme.Gray500
import com.mundcode.designsystem.theme.MTRed
import com.mundcode.domain.model.Exam
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.muntam.util.asMTDateText
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class ExamModel( // todo 수정
    var id: Int = 0,
    val subjectId: Int,
    val name: String,
    val isFavorite: Boolean = false,
    val timeLimit: Long,
    val lastAt: Long? = null, // (시험 종료이든 또는 중간에 나갔든) 마지막으로 기록된 시험진행 시간
    val createdAt: Instant,
    val endAt: Instant? = null, // 시험이 끝났을 때 시간
    val lastQuestionNumber: Int? = null, // 시험 기록 중 마지막으로 푼 문제
    val deletedAt: Instant? = null, // 소프트 딜리트 용도
    val state: ExamState = ExamState.READY
) {
    val createdAtText: String = createdAt.asMTDateText()

    val expiredTimeText: String? = obtainExpiredTime()
    val expiredTimeTextColor: Color = obtainExpiredTimeTextColor()

    private fun obtainExpiredTime(): String? {
        lastAt ?: return null

        val diff = timeLimit - lastAt
        if (diff >= 0) return null

        val sec = (diff / 1000) % 60
        val min = (diff / 1000 / 60) % 60
        val hour = (diff / 1000 / 60 / 60) % 24

        return "%02d.%02d.%02d"
    }

    private fun obtainExpiredTimeTextColor(): Color {
        return when {
            lastAt == null -> Gray500
            timeLimit - lastAt >= 0 -> Gray500
            else -> MTRed
        }
    }
}

fun Exam.asStateModel() = ExamModel( // todo 수정
    id = id,
    subjectId = subjectId,
    name = name,
    isFavorite = isFavorite,
    timeLimit = timeLimit,
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
    timeLimit = timeLimit,
    createdAt = createdAt,
    endAt = endAt,
    lastAt = lastAt,
    lastQuestionNumber = lastQuestionNumber,
    deletedAt = deletedAt,
    state = state
)

fun createExamModels(
    size: Int,
    subjectId: Int = 0
) = (1..size).map {
    createExamModel(
        id = it,
        subjectId = subjectId,
    )
}

fun createExamModel(
    id: Int,
    subjectId: Int
) = ExamModel(
    id = id,
    subjectId = subjectId,
    name = "테스트 시험 이름 : $id",
    createdAt = Clock.System.now(),
    timeLimit = id * 100000L
)
