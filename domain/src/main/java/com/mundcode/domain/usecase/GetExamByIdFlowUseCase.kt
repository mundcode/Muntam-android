package com.mundcode.domain.usecase

import com.mundcode.domain.model.Exam
import com.mundcode.domain.repository.ExamRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class GetExamByIdFlowUseCase @Inject constructor(
    private val examRepository: ExamRepository
) {
    operator fun invoke(id: Int): Flow<Exam> = examRepository.getExamByIdFlow(id)
}
