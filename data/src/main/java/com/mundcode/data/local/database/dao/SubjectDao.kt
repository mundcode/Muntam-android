package com.mundcode.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mundcode.data.local.database.model.SubjectEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.Instant

@Dao
abstract class SubjectDao : BaseDao<SubjectEntity> {
    @Query(
        value = """
            SELECT * FROM subjects WHERE deleted_at IS NULL
            
        """
    )
    abstract fun getSubjects(): Flow<List<SubjectEntity>>

    fun getSubjectsDistinctUntilChanged() = getSubjects().distinctUntilChanged()

    @Query(
        value = """
            SELECT * FROM subjects WHERE deleted_at IS NULL AND id = :id
        """
    )
    abstract fun getSubjectById(id: Int): SubjectEntity

    @Query(
        value = """
            SELECT * FROM subjects WHERE deleted_at IS NULL AND id = :id
        """
    )
    abstract fun getSubjectByIdFlow(id: Int): Flow<SubjectEntity>

    fun getSubjectDistinctUntilChanged(id: Int) = getSubjectByIdFlow(id).distinctUntilChanged()

    @Update
    abstract suspend fun updateSubjects(entities: List<SubjectEntity>)

    @Update
    abstract suspend fun updateSubject(entity: SubjectEntity)

    @Query(
        value = """
            UPDATE subjects SET deleted_at=:deletedAt WHERE id = :id
            """
    )
    abstract suspend fun deleteSubjects(id: Int, deletedAt: Instant)

    @Query(
        value = """
            UPDATE exams SET deleted_at=:deletedAt WHERE subject_id = :id
        """
    )
    abstract suspend fun deleteExamsBySubjectId(id: Int, deletedAt: Instant) // todo test

    @Transaction
    open suspend fun deleteSubjectsWithCasacde(subjectId: Int, deletedAt: Instant) {
        deleteSubjects(subjectId, deletedAt)
        deleteExamsBySubjectId(subjectId, deletedAt)
        // todo 문제도 삭제, 알림 넣었던 것도 삭제
    }
}
