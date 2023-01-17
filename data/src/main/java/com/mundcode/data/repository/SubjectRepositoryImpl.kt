package com.mundcode.data.repository

import com.mundcode.data.local.database.dao.ExamDao
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
    private val examDao: ExamDao
) : SubjectRepository {
    override suspend fun insertSubjects(entities: List<Subject>) {
        subjectDao.insert(entities.map(Subject::asEntity))
    }

    override fun getSubjects(): Flow<List<Subject>> {
        return subjectDao.getSubjects().map { list ->
            list.map(SubjectEntity::asExternalModel)
        }
    }

    override suspend fun deleteSubjectRepository(ids: List<Int>) {
        subjectDao.deleteSubjectsAndExams(ids, Clock.System.now())
    }
}