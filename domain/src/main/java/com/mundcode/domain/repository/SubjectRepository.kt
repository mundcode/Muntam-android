package com.mundcode.domain.repository

import com.mundcode.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {
    suspend fun insertSubjects(entities: List<Subject>)

    fun getSubjects(): Flow<List<Subject>>
}