package com.mundcode.domain.usecase

import com.mundcode.domain.model.Exam
import com.mundcode.domain.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetExamsUseCase @Inject constructor(
    private val examRepository: ExamRepository
) {
    operator fun invoke(subjectId: Int): Flow<List<Exam>> = examRepository.getExams(subjectId)
}
