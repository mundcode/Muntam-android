package com.mundcode.data.repository

import com.mundcode.data.local.database.dao.QuestionDao
import com.mundcode.data.local.database.model.asEntity
import com.mundcode.data.local.database.model.asExternalModel
import com.mundcode.domain.model.Question
import com.mundcode.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao
) : QuestionRepository {
    override suspend fun insertQuestion(question: Question) =
        questionDao.insert(question.asEntity())

    override suspend fun insertQuestions(questions: List<Question>) =
        questionDao.insertAll(questions.map { it.asEntity() })

    override fun getQuestions(examId: Int): Flow<List<Question>> =
        questionDao.getQuestions(examId).map { questions ->
            questions.map { question ->
                question.asExternalModel()
            }
        }

    override fun getQuestionExamId(examId: Int, questionNumber: Int): Flow<Question> =
        questionDao.getQuestionExamId(examId, questionNumber).map {
            it.asExternalModel()
        }

    override fun getQuestionById(id: Int): Flow<Question> =
        questionDao.getQuestionById(id).map {
            it.asExternalModel()
        }

    override suspend fun updateQuestion(question: Question) =
        questionDao.updateQuestion(question.asEntity())
}
