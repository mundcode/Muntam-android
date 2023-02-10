package com.mundcode.domain.usecase

import com.mundcode.domain.model.Question
import com.mundcode.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(examId: Int): Flow<List<Question>> = questionRepository.getQuestions(examId)
}