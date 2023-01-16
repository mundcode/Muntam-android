package com.mundcode.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.mundcode.data.local.database.model.SubjectEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
abstract class SubjectDao : BaseDao<SubjectEntity> {
    @Query(
        value = """
            SELECT * FROM subjects
            
        """
    )
    abstract fun getSubjects(): Flow<List<SubjectEntity>>

    @Query(
        value = """
            SELECT * FROM subjects
            WHERE id = :id
        """
    )
    abstract fun getSubject(id: Int): Flow<SubjectEntity>

    @Update
    abstract suspend fun updateSubjects(entities: List<SubjectEntity>)

    @Query(
        value = """
            UPDATE subjects SET deleted_at=:deletedAt WHERE id in (:ids)
            """
    )
    abstract suspend fun deleteSubjects(ids: List<Int>, deletedAt: Instant)
}
