package com.mundcode.data.repository

import com.mundcode.data.local.database.dao.QuestionDao
import com.mundcode.domain.model.Question
import com.mundcode.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao
) : QuestionRepository {
    override suspend fun insertQuestion(question: Question) {
        TODO("Not yet implemented")
    }

    override suspend fun insertQuestions(questions: List<Question>) {
        TODO("Not yet implemented")
    }

    override fun getQuestions(examId: Int): Flow<List<Question>> {
        TODO("Not yet implemented")
    }

    override fun getQuestionExamId(examId: Int, questionNumber: Int): Flow<Question> {
        TODO("Not yet implemented")
    }

    override fun getQuestionById(id: Int): Flow<Question> {
        TODO("Not yet implemented")
    }

    override suspend fun updateQuestion(question: Question) {
        TODO("Not yet implemented")
    }
}