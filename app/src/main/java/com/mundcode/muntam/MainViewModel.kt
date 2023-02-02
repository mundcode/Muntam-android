package com.mundcode.muntam

import androidx.lifecycle.ViewModel
import com.mundcode.domain.repository.ExamRepository
import com.mundcode.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val examRepository: ExamRepository
) : ViewModel()
