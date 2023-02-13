package com.mundcode.data.repository

import com.mundcode.data.local.database.dao.QuestionDao
import com.mundcode.data.local.database.model.asEntity
import com.mundcode.domain.model.Question
import com.mundcode.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao
) : QuestionRepository {
    override suspend fun insertQuestion(question: Question) =
        questionDao.insert(question.asEntity())

    override suspend fun insertQuestions(questions: List<Question>) =
        questionDao.insertAll(questions.map { it.asEntity() })

    override fun getQuestions(examId: Int): Flow<List<Question>> =
        questionDao.getQuestions(examId)

    override fun getQuestionExamId(examId: Int, questionNumber: Int): Flow<Question> =
        questionDao.getQuestionExamId(examId, questionNumber)

    override fun getQuestionById(id: Int): Flow<Question> = questionDao.getQuestionById(id)

    override suspend fun updateQuestion(question: Question) = questionDao.updateQuestion(question)
}
