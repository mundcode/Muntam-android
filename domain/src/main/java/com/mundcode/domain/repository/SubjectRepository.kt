package com.mundcode.domain.repository

import com.mundcode.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {
    suspend fun insertSubject(subject: Subject)

    fun getSubjects(): Flow<List<Subject>>

    suspend fun getSubjectById(id: Int): Subject

    suspend fun deleteSubjectRepository(id: Int)

    suspend fun updateSubject(subject: Subject)
}
