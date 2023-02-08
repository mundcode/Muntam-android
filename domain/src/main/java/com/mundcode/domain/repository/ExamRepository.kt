package com.mundcode.domain.repository

import com.mundcode.domain.model.Exam
import kotlinx.coroutines.flow.Flow

interface ExamRepository {
    fun getExams(subjectId: Int): Flow<List<Exam>>

    fun getExamById(id: Int): Flow<Exam>

    suspend fun insertExam(exam: Exam)

    suspend fun updateExam(exam: Exam)

    suspend fun deleteExam(id: Int)
}
