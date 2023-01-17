package com.mundcode.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mundcode.data.local.database.model.ExamEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
abstract class ExamDao : BaseDao<ExamEntity> {
    @Query(
        value = """
            SELECT * FROM exams WHERE deleted_at IS NULL
        """
    )
    abstract fun getExams(): Flow<List<ExamEntity>>

    @Query(
        value = """
            UPDATE exams SET deleted_at=:deletedAt WHERE id IN (:ids)
        """
    )
    abstract suspend fun deleteExams(ids: List<Int>, deletedAt: Instant)
}