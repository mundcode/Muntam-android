package com.mundcode.domain.usecase

import com.mundcode.domain.model.Subject
import com.mundcode.domain.repository.SubjectRepository
import javax.inject.Inject
import javax.inject.Singleton

// todo 시험리스트화면에서 과목데이터를 조회하고 싶을 때 사용.
@Singleton
class GetSubjectByIdUseCase @Inject constructor(
    private val subjectRepository: SubjectRepository
) {
    suspend operator fun invoke(id: Int): Subject = subjectRepository.getSubjectById(id)
}
