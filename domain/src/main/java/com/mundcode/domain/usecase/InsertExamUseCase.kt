package com.mundcode.domain.usecase

import com.mundcode.domain.model.Exam
import com.mundcode.domain.repository.ExamRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertExamUseCase @Inject constructor(
    private val examRepository: ExamRepository
) {
    suspend operator fun invoke(exam: Exam): Int {
        return examRepository.insertExam(exam)
    }
}
