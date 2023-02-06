package com.mundcode.data.repository

import com.mundcode.data.local.database.dao.ExamDao
import com.mundcode.data.local.database.model.ExamEntity
import com.mundcode.data.local.database.model.asEntity
import com.mundcode.data.local.database.model.asExternalModel
import com.mundcode.domain.model.Exam
import com.mundcode.domain.repository.ExamRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class ExamRepositoryImpl @Inject constructor(
    private val examDao: ExamDao
) : ExamRepository {
    override suspend fun insertExam(exam: Exam) {
        examDao.insert(exam.asEntity())
    }

    override fun getExams(): Flow<List<Exam>> {
        return examDao.getExams().map { list ->
            list.map(ExamEntity::asExternalModel)
        }
    }

    override suspend fun updateExam(exam: Exam) {
        examDao.updateExam(exam.asEntity())
    }

    override suspend fun deleteExam(id: Int) {
       examDao.deleteExamWithCasacade(id, Clock.System.now())
    }
}
