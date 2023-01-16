package com.mundcode.domain.repository

import com.mundcode.domain.model.Subject
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface SubjectRepository {
    suspend fun insertSubjects(entities: List<Subject>)

    fun getSubjects(): Flow<List<Subject>>

    suspend fun deleteSubjectRepository(ids: List<Int>)
}