package com.mundcode.data.repository

import com.mundcode.data.local.database.dao.ExamDao
import com.mundcode.data.local.database.dao.SubjectDao
import com.mundcode.data.local.database.model.SubjectEntity
import com.mundcode.data.local.database.model.asEntity
import com.mundcode.data.local.database.model.asExternalModel
import com.mundcode.domain.model.Subject
import com.mundcode.domain.repository.SubjectRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class SubjectRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao,
    private val examDao: ExamDao
) : SubjectRepository {
    override suspend fun insertSubject(subject: Subject) {
        subjectDao.insert(listOf(subject.asEntity()))
    }

    override fun getSubjects(): Flow<List<Subject>> {
        return subjectDao.getSubjects().map { list ->
            list.map(SubjectEntity::asExternalModel)
        }
    }

    override suspend fun deleteSubjectRepository(id: Int) {
        subjectDao.deleteSubjectsAndExams(listOf(id), Clock.System.now())
    }

    override suspend fun updateSubject(subject: Subject) {
        // do nothing
    }
}
