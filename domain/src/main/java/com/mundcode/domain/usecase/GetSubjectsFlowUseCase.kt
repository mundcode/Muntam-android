package com.mundcode.domain.usecase

import com.mundcode.domain.repository.SubjectRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSubjectsFlowUseCase @Inject constructor(
    private val subjectRepository: SubjectRepository
) {
    operator fun invoke() = subjectRepository.getSubjectsFlow()
}
