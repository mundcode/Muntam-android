package com.mundcode.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.mundcode.data.local.database.model.QuestionEntity
import com.mundcode.domain.model.Question
import kotlinx.coroutines.flow.Flow

@Dao
abstract class QuestionDao : BaseDao<QuestionEntity> {
    @Query(
        value = """
            SELECT * FROM questions WHERE deleted_at IS NULL AND exam_id = :examId
        """
    )
    abstract fun getQuestions(examId: Int): Flow<List<QuestionEntity>>

    @Query(
        value = """
            SELECT * FROM questions WHERE deleted_at IS NULL AND exam_id = :examId AND question_number = :questionNumber
        """
    )
    abstract fun getQuestionExamId(examId: Int, questionNumber: Int): Flow<QuestionEntity>

    @Query(
        value = """
            SELECT * FROM questions WHERE deleted_at IS NULL
        """
    )
    abstract fun getQuestionById(id: Int): Flow<QuestionEntity>

    @Update
    abstract suspend fun updateQuestion(question: QuestionEntity)
}