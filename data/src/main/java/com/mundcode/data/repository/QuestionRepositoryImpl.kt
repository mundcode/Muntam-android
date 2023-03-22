package com.mundcode.data.repository

import com.mundcode.data.local.database.dao.QuestionDao
import com.mundcode.data.local.database.model.asEntity
import com.mundcode.data.local.database.model.asExternalModel
import com.mundcode.domain.model.Question
import com.mundcode.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao
) : QuestionRepository {
    override suspend fun insertQuestion(question: Question) =
        questionDao.insert(question.asEntity())

    override suspend fun insertQuestions(questions: List<Question>) =
        questionDao.insertAll(questions.map { it.asEntity() })

    override fun getQuestionsByExamIdFlow(examId: Int): Flow<List<Question>> =
        questionDao.getQuestionsByExamIdFlow(examId).map { questions ->
            questions.map { question ->
                question.asExternalModel()
            }
        }

    override fun getQuestionsByExamIdDescFlow(examId: Int): Flow<List<Question>> {
        return questionDao.getQuestionsByExamIdDescFlow(examId).map { questions ->
            questions.map { question ->
                question.asExternalModel()
            }
        }
    }

    override fun getQuestionByExamIdWrongFirst(examId: Int): Flow<List<Question>> {
        return questionDao.getQuestionByExamIdWrongFirst(examId).map { questions ->
            questions.map { question ->
                question.asExternalModel()
            }
        }
    }

    override fun getQuestionByExamId(examId: Int): List<Question> =
        questionDao.getQuestionByExamId(examId).map { it.asExternalModel() }

    override suspend fun getQuestionByQuestionId(questionId: Int): Question =
        questionDao.getQuestionByQuestionId(questionId).asExternalModel()

    override fun getQuestionByQuestionNumberFlow(examId: Int, questionNumber: Int): Flow<Question> =
        questionDao.getQuestionByQuestionNumber(examId, questionNumber).map {
            it.asExternalModel()
        }

    override fun getQuestionById(id: Int): Flow<Question> =
        questionDao.getQuestionById(id).map {
            it.asExternalModel()
        }

    override suspend fun updateQuestion(question: Question) =
        questionDao.updateQuestion(question.asEntity())
}
