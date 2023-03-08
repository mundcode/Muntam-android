package com.mundcode.domain.usecase

import com.mundcode.domain.model.Exam
import com.mundcode.domain.repository.ExamRepository
import javax.inject.Inject

class GetExamByIdUseCase @Inject constructor(
    private val examRepository: ExamRepository
) {
    suspend operator fun invoke(examId: Int): Exam = examRepository.getExamById(examId)
}
