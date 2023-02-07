package com.mundcode.domain.usecase

import com.mundcode.domain.model.Exam
import com.mundcode.domain.repository.ExamRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateExamUseCase @Inject constructor(
    private val examRepository: ExamRepository
) {
    suspend operator fun invoke(exam: Exam) {
        examRepository.updateExam(exam)
    }
}
