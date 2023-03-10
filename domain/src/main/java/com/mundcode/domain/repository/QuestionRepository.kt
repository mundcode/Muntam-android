package com.mundcode.domain.repository

import com.mundcode.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun insertQuestion(question: Question): Long

    suspend fun insertQuestions(questions: List<Question>)

    fun getQuestionsByExamIdFlow(examId: Int): Flow<List<Question>>

    fun getQuestionByExamId(examId: Int): List<Question>

    fun getQuestionByQuestionId(examId: Int, questionNumber: Int): Flow<Question>

    fun getQuestionById(id: Int): Flow<Question>

    suspend fun updateQuestion(question: Question)
}
