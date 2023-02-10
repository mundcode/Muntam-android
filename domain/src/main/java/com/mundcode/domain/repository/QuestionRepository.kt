package com.mundcode.domain.repository

import com.mundcode.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun insertQuestion(exam: Question)

    suspend fun insertQuestions(exmas: List<Question>)

    fun getQuestions(examId: Int): Flow<List<Question>>

    fun getQuestionExamId(examId: Int, questionNumber: String): Flow<Question>

    fun getQuestionById(questionId: Int):Flow<Question>

    suspend fun updateQuestionId(question: Question)
}