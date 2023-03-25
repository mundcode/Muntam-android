package com.mundcode.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.mundcode.data.local.database.model.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class QuestionDao : BaseDao<QuestionEntity> {
    @Query(
        value = """
            SELECT * FROM questions WHERE deleted_at IS NULL AND subject_id = :subjectId
        """
    )
    abstract fun getQuestionsBySubjectId(subjectId: Int): List<QuestionEntity>

    @Query(
        value = """
            SELECT * FROM questions WHERE deleted_at IS NULL AND exam_id = :examId
        """
    )
    abstract fun getQuestionsByExamIdFlow(examId: Int): Flow<List<QuestionEntity>>

    @Query(
        value = """
            SELECT * FROM questions WHERE deleted_at IS NULL AND exam_id = :examId ORDER BY lapsed_time DESC
        """
    )
    abstract fun getQuestionsByExamIdDescFlow(examId: Int): Flow<List<QuestionEntity>>

    @Query(
        value = """
            SELECT * FROM questions WHERE deleted_at IS NULL AND exam_id = :examId ORDER BY is_correct
        """
    )
    abstract fun getQuestionByExamIdWrongFirst(examId: Int): Flow<List<QuestionEntity>>

    @Query(
        value = """
            SELECT * FROM questions WHERE deleted_at IS NULL AND exam_id = :examId
        """
    )
    abstract fun getQuestionByExamId(examId: Int): List<QuestionEntity>

    @Query(
        value = """
            SELECT * FROM questions WHERE deleted_at IS NULL AND id = :id
        """
    )
    abstract fun getQuestionByQuestionId(id: Int): QuestionEntity

    @Query(
        value = """
            SELECT * FROM questions WHERE deleted_at IS NULL AND exam_id = :examId AND question_number = :questionNumber
        """
    )
    abstract fun getQuestionByQuestionNumber(examId: Int, questionNumber: Int): Flow<QuestionEntity>

    // 파라미터를 쓰지 않으면 Unused Parameter 에러를 던진다.
    @Query(
        value = """
            SELECT * FROM questions WHERE deleted_at IS NULL AND id = :id
        """
    )
    abstract fun getQuestionById(id: Int): Flow<QuestionEntity>

    @Update
    abstract suspend fun updateQuestion(question: QuestionEntity)
}
