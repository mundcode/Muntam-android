package com.mundcode.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mundcode.data.local.database.model.SubjectEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
abstract class SubjectDao : BaseDao<SubjectEntity> {
    @Query(
        value = """
            SELECT * FROM subjects WHERE deleted_at IS NULL
            
        """
    )
    abstract fun getSubjects(): Flow<List<SubjectEntity>>

    @Query(
        value = """
            SELECT * FROM subjects 
            WHERE deleted_at IS NULL AND id = :id
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

    @Query(
        value = """
            UPDATE exams SET deleted_at=:deletedAt WHERE id in (:id)
        """
    )
    abstract suspend fun deleteExamsBySubjectId(id: Int, deletedAt: Instant)

    @Transaction
    open suspend fun deleteSubjectsAndExams(ids: List<Int>, deletedAt: Instant) {
        deleteSubjects(ids, deletedAt)
        ids.forEach {
            deleteExamsBySubjectId(it, deletedAt)
            // todo 문제 삭제, 알림 넣었던 것도 삭제
        }
    }
}
