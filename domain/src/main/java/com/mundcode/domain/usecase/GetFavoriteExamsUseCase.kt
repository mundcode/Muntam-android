package com.mundcode.domain.usecase

import com.mundcode.domain.model.Exam
import com.mundcode.domain.repository.ExamRepository
import com.mundcode.domain.repository.SubjectRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavoriteExamsUseCase @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val examRepository: ExamRepository
) {

    operator fun invoke(): Flow<Map<String, List<Exam>>> {
        return examRepository.getFavoriteExams().map { list ->
            val group = list.groupBy { it.subjectId }
            val result = mutableMapOf<String, List<Exam>>()
            group.forEach { (subjectId, exams) ->
                val subject = subjectRepository.getSubjectById(subjectId)
                result[subject.name] = exams
            }
            result
        }
    }
}
