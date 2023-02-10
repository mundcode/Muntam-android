package com.mundcode.domain.usecase

import com.mundcode.domain.model.Question
import com.mundcode.domain.repository.QuestionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertQuestionsExamUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(questions: List<Question>) {
        questionRepository.insertQuestions(questions)
    }
}
