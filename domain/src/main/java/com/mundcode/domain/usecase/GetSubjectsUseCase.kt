package com.mundcode.domain.usecase

import com.mundcode.domain.repository.SubjectRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSubjectsUseCase @Inject constructor(
    private val subjectRepository: SubjectRepository
) {
    operator fun invoke() = subjectRepository.getSubjectsFlow()
}
