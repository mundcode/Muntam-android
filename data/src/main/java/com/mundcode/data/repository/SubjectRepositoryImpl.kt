package com.mundcode.data.repository

import com.mundcode.data.local.database.dao.SubjectDao
import com.mundcode.data.local.database.model.SubjectEntity
import com.mundcode.data.local.database.model.asEntity
import com.mundcode.data.local.database.model.asExternalModel
import com.mundcode.domain.model.Subject
import com.mundcode.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao,
) : SubjectRepository {
    override suspend fun insertSubject(subject: Subject) {
        subjectDao.insert(subject.asEntity())
    }

    override fun getSubjectsFlow(): Flow<List<Subject>> {
        return subjectDao.getSubjectsDistinctUntilChanged().map { list ->
            list.map(SubjectEntity::asExternalModel)
        }
    }

    override suspend fun getSubjectById(id: Int): Subject {
        return subjectDao.getSubjectById(id).asExternalModel()
    }

    override suspend fun getSubjectByIdFlow(id: Int): Flow<Subject> {
        return subjectDao.getSubjectDistinctUntilChanged(id).map { it.asExternalModel() }
    }

    override suspend fun updateSubject(subject: Subject) {
        subjectDao.updateSubject(subject.asEntity())
    }

    override suspend fun deleteSubjectRepository(id: Int) {
        subjectDao.deleteSubjectsWithCasacde(id, Clock.System.now())
    }
}
