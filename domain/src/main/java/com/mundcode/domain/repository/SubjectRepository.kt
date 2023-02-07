package com.mundcode.domain.repository

import com.mundcode.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {
    suspend fun insertSubject(subject: Subject)

    fun getSubjects(): Flow<List<Subject>>

    fun getSubjectById(id: Int): Flow<Subject>

    suspend fun deleteSubjectRepository(id: Int)

    suspend fun updateSubject(subject: Subject)
}
