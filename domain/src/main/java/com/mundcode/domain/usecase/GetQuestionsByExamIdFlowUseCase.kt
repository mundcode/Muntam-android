package com.mundcode.domain.usecase

import com.mundcode.domain.model.Question
import com.mundcode.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetQuestionsByExamIdFlowUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(examId: Int): Flow<List<Question>> =
        questionRepository.getQuestionsByExamIdFlow(examId)
}
