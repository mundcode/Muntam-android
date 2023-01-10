package com.mundcode.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mundcode.data.local.database.dao.SubjectDao
import com.mundcode.data.local.database.model.ExamEntity
import com.mundcode.data.local.database.model.QuestionEntity
import com.mundcode.data.local.database.model.SubjectEntity

@Database(
    entities = [
        SubjectEntity::class,
        ExamEntity::class,
        QuestionEntity::class
    ],
    version = 1
)
abstract class MuntamDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
}
