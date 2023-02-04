package com.mundcode.muntam.presentation.ui.main.subjects

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mundcode.domain.model.Exam
import com.mundcode.domain.model.ExamState
import com.mundcode.domain.repository.ExamRepository
import com.mundcode.domain.repository.SubjectRepository
import com.mundcode.muntam.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val examRepository: ExamRepository
) : BaseViewModel() {

    fun getS() = viewModelScope.launch {
        subjectRepository.getSubjects().collectLatest {
            Log.d("SR-N", "getS : $it")
        }
    }

    fun setE() = viewModelScope.launch {
        examRepository.insertExams(
            (10..20).map {
                Exam(
                    subjectId = it,
                    name = "$it",
                    isFavorite = false,
                    createdAt = Clock.System.now(),
                    state = ExamState.RUNNING
                )
            }
        )
        Log.d("SR-N", "setE 완료")
    }

    fun getE() = viewModelScope.launch {
        examRepository.getExams().collectLatest {
            Log.d("SR-N", "getE : $it")
        }
    }
}
