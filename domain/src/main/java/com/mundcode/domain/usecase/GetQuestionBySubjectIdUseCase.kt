package com.mundcode.domain.usecase

import com.mundcode.domain.model.Question
import com.mundcode.domain.repository.QuestionRepository
import javax.inject.Inject

class GetQuestionBySubjectIdUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(subjectId: Int): List<Question> =
        questionRepository.getQuestionsBySubjectId(subjectId)
}
