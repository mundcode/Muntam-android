package com.mundcode.domain.usecase

import com.mundcode.domain.repository.ExamRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteExamUseCase @Inject constructor(
    private val examRepository: ExamRepository
) {
    suspend operator fun invoke(id: Int) {
        examRepository.deleteExam(id)
    }
}
