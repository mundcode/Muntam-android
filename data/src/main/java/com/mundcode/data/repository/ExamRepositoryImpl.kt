package com.mundcode.data.repository

import com.mundcode.data.local.database.dao.ExamDao
import com.mundcode.data.local.database.model.ExamEntity
import com.mundcode.data.local.database.model.asEntity
import com.mundcode.data.local.database.model.asExternalModel
import com.mundcode.domain.model.Exam
import com.mundcode.domain.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

class ExamRepositoryImpl @Inject constructor(
    private val examDao: ExamDao
) : ExamRepository {
    override suspend fun insertExams(exmas: List<Exam>) {
        examDao.insert(exmas.map(Exam::asEntity))
    }

    override fun getExams(): Flow<List<Exam>> {
        return examDao.getExams().map { list ->
            list.map(ExamEntity::asExternalModel)
        }
    }

    override suspend fun deleteExams(ids: List<Int>) {
        examDao.deleteExams(ids, Clock.System.now())
    }
}