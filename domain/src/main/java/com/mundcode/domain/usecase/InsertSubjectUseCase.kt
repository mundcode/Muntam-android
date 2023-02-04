package com.mundcode.domain.usecase

import com.mundcode.domain.model.Subject
import com.mundcode.domain.repository.SubjectRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertSubjectUseCase @Inject constructor(
    private val subjectRepository: SubjectRepository
) {
    suspend operator fun invoke(subject: Subject) {
        subjectRepository.insertSubject(subject)
    }
}
