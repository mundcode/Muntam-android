package com.mundcode.data.di

import com.mundcode.data.repository.ExamRepositoryImpl
import com.mundcode.data.repository.SubjectRepositoryImpl
import com.mundcode.domain.repository.ExamRepository
import com.mundcode.domain.repository.SubjectRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindSubjectRepository(subjectRepositoryImpl: SubjectRepositoryImpl): SubjectRepository

    @Binds
    @Singleton
    fun bindExamRepository(examRepository: ExamRepositoryImpl): ExamRepository
}