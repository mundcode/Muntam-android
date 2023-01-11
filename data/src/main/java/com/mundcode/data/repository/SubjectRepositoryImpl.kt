package com.mundcode.data.repository

import com.mundcode.data.local.database.dao.SubjectDao
import com.mundcode.data.local.database.model.SubjectEntity
import com.mundcode.domain.model.Subject
import com.mundcode.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao
) : SubjectRepository {
    override suspend fun insertSubjects(entities: List<Subject>) {
        subjectDao.insertSubjects(entities.map {
            SubjectEntity(
                name = it.name,
                totalQuestionNumber = it.totalQuestionNumber,
                timeLimit = it.timeLimit,
                createdAt = it.createdAt
            )
        })
    }

    override fun getSubjects(): Flow<List<Subject>> {
        return subjectDao.getSubjects().map { list ->
            list.map {
                Subject(
                    name = it.name,
                    totalQuestionNumber = it.totalQuestionNumber,
                    timeLimit = it.timeLimit,
                    createdAt = it.createdAt
                )
            }
        }
    }
}