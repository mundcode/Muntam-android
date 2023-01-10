package com.mundcode.data.local.database

import com.mundcode.data.local.database.dao.SubjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesSubjectsDao(
        database: MuntamDatabase
    ): SubjectDao = database.subjectDao()
}
