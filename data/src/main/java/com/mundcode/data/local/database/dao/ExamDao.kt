package com.mundcode.data.local.database.dao

import androidx.room.Query
import com.mundcode.data.local.database.model.ExamEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

abstract class ExamDao : BaseDao<ExamDao> {
    @Query(
        value = """
            SELECT * FROM exams
        """
    )
    abstract fun getExams(): Flow<List<ExamEntity>>

    @Query(
        value = """
            UPDATE exams SET deleted_at=:deletedAt WHERE id in (:ids)
        """
    )
    abstract suspend fun deleteExams(ids: List<Int>, deletedAt: Instant)
}