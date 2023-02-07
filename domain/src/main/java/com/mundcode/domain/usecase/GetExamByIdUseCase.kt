package com.mundcode.domain.usecase

import com.mundcode.domain.model.Exam
import com.mundcode.domain.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// todo 문제리스트(시험상세)화면에서 시험관련 데이터를 조회하고 싶을 때 사용.
@Singleton
class GetExamByIdUseCase @Inject constructor(
    private val examRepository: ExamRepository
) {
    operator fun invoke(id: Int): Flow<Exam> = examRepository.getExamById(id)
}
