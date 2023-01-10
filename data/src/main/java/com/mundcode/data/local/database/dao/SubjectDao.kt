package com.mundcode.data.local.database.dao

import androidx.room.*
import com.mundcode.data.local.database.model.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSubjects(entities: List<SubjectEntity>)

    @Query(
        value = """
            SELECT * FROM subjects
            
        """
    )
    fun getSubjects(): Flow<List<SubjectEntity>>

    @Query(
        value = """
            SELECT * FROM subjects
            WHERE id = :id
        """
    )
    fun getSubject(id: Int): Flow<SubjectEntity>

    @Update
    suspend fun updateSubjects(entities: List<SubjectEntity>)

    @Query(
        value = """
            DELETE FROM subjects
            WHERE id in (:ids)
        """
    )
    suspend fun deleteSubjects(ids: List<Int>)

}