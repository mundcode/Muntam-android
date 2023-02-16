package com.mundcode.domain.repository

import com.mundcode.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun insertQuestion(question: Question)

    suspend fun insertQuestions(questions: List<Question>)

    fun getQuestionsByExamId(examId: Int): Flow<List<Question>>

    fun getQuestionByQuestionId(examId: Int, questionNumber: Int): Flow<Question>

    fun getQuestionById(id: Int): Flow<Question>

    suspend fun updateQuestion(question: Question)
}
