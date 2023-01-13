package com.mundcode.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "exams",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subject_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExamEntity(
    @ColumnInfo(name = "subject_id")
    val subjectId: Int,
    val name: String,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "end_at")
    val endAt: Long?,
    @ColumnInfo(name = "modified_at")
    val modifiedAt: Long?,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long?,
    val state: String // todo enum 으로 대체
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
