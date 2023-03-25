package com.mundcode.data.repository

import com.mundcode.data.local.database.dao.ExamDao
import com.mundcode.data.local.database.model.ExamEntity
import com.mundcode.data.local.database.model.asEntity
import com.mundcode.data.local.database.model.asEntityWithModify
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
    override suspend fun insertExam(exam: Exam): Long {
        return examDao.insert(exam.asEntity())
    }

    override suspend fun getExamById(id: Int): Exam {
        return examDao.getExamById(id).asExternalModel()
    }

    override fun getExamByIdFlow(id: Int): Flow<Exam> {
        return examDao.getExamByIdDistinctUntilChanged(id).map(ExamEntity::asExternalModel)
    }

    override fun getExams(subjectId: Int): Flow<List<Exam>> {
        return examDao.getExams(subjectId).map { list ->
            list.map(ExamEntity::asExternalModel)
        }
    }

    override fun getFavoriteExams(): Flow<List<Exam>> {
        return examDao.getFavoriteExams().map { list ->
            list.map(ExamEntity::asExternalModel)
        }
    }

    override suspend fun updateExam(exam: Exam) {
        examDao.updateExam(exam.asEntityWithModify())
    }

    override suspend fun deleteExam(id: Int) {
        examDao.deleteExamWithCasacade(id, Clock.System.now())
    }
}
