package com.mundcode.domain.usecase

import com.mundcode.domain.model.Question
import com.mundcode.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetQuestionUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(id: Int): Flow<Question> = questionRepository.getQuestionById(id = id)
}
