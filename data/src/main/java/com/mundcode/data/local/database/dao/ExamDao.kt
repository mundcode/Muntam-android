package com.mundcode.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mundcode.data.local.database.model.ExamEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
abstract class ExamDao : BaseDao<ExamEntity> {
    @Query(
        value = """
            SELECT * FROM exams WHERE deleted_at IS NULL AND subject_id = :subjectId
        """
    )
    abstract fun getExams(subjectId: Int): Flow<List<ExamEntity>>

    @Query(
        value = """
            SELECT * FROM exams WHERE deleted_at IS NULL AND exam_id = :examId
        """
    )
    abstract fun getExam(examId: Int): ExamEntity

    @Query(
        value = """
            SELECT * FROM exams WHERE deleted_at IS NULL AND id = :id
        """
    )
    abstract fun getExamById(id: Int): Flow<ExamEntity>

    @Query(
        value = """
            UPDATE exams SET deleted_at=:deletedAt WHERE id = :id
        """
    )
    abstract suspend fun deleteExam(id: Int, deletedAt: Instant)

    @Update
    abstract suspend fun updateExam(exam: ExamEntity)

    @Transaction
    open suspend fun deleteExamWithCasacade(id: Int, deletedAt: Instant) {
        deleteExam(id, deletedAt)
        // todo 시험삭제하면서 딸린 문제까지 지우는 @Transaction 작성하고 deleteExam 대체
    }
}
