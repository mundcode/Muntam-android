package com.mundcode.domain.repository

import com.mundcode.domain.model.Exam
import kotlinx.coroutines.flow.Flow

interface ExamRepository {
    fun getExams(subjectId: Int): Flow<List<Exam>>

    suspend fun getExamById(examId: Int): Exam

    fun getExamByIdFlow(id: Int): Flow<Exam>

    suspend fun insertExam(exam: Exam): Long

    suspend fun updateExam(exam: Exam)

    suspend fun deleteExam(id: Int)
}
