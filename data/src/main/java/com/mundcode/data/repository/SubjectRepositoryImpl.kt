package com.mundcode.data.repository

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
) : SubjectRepository {
    override suspend fun insertSubject(subject: Subject) {
        subjectDao.insert(subject.asEntity())
    }

    override fun getSubjects(): Flow<List<Subject>> {
        return subjectDao.getSubjectsDistinctUntilChanged().map { list ->
            list.map(SubjectEntity::asExternalModel)
        }
    }

    // todo 시험 리스트 화면에서 시험객체로 과목을 조회하고 싶을 때 사용.
    override suspend fun getSubjectById(id: Int): Flow<Subject> {
        return subjectDao.getSubjectById(id).map(SubjectEntity::asExternalModel)
    }

    override suspend fun updateSubject(subject: Subject) {
        subjectDao.updateSubject(subject.asEntity())
    }

    override suspend fun deleteSubjectRepository(id: Int) {
        subjectDao.deleteSubjectsWithCasacde(id, Clock.System.now())
    }
}
