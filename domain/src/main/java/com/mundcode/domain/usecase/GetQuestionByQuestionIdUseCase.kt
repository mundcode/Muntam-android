package com.mundcode.domain.usecase

import com.mundcode.domain.model.Question
import com.mundcode.domain.repository.QuestionRepository
import javax.inject.Inject

class GetQuestionByQuestionIdUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(questionId: Int): Question =
        questionRepository.getQuestionByQuestionId(questionId)
}
