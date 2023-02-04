package com.mundcode.domain.usecase

import com.mundcode.domain.repository.SubjectRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteSubjectUseCase @Inject constructor(
    private val subjectRepository: SubjectRepository
) {
    suspend operator fun invoke(id: Int) {
        subjectRepository.deleteSubjectRepository(id)
    }
}
