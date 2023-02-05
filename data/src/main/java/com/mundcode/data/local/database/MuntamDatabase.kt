package com.mundcode.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mundcode.data.local.database.dao.ExamDao
import com.mundcode.data.local.database.dao.SubjectDao
import com.mundcode.data.local.database.model.ExamEntity
import com.mundcode.data.local.database.model.QuestionEntity
import com.mundcode.data.local.database.model.SubjectEntity
import com.mundcode.data.local.database.util.InstantConverters

@Database(
    entities = [
        SubjectEntity::class,
        ExamEntity::class,
        QuestionEntity::class
    ],
    version = 1
)
@TypeConverters(InstantConverters::class)
abstract class MuntamDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun examDao(): ExamDao
}
