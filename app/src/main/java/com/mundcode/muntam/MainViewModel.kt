package com.mundcode.muntam

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundcode.domain.model.Exam
import com.mundcode.domain.model.ExamState
import com.mundcode.domain.model.Subject
import com.mundcode.domain.repository.ExamRepository
import com.mundcode.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val examRepository: ExamRepository
) : ViewModel() {

    fun setS() = viewModelScope.launch {
        subjectRepository.insertSubjects(
            (10..20).map {
                Subject(
                    name = "과목명$it",
                    totalQuestionNumber = it,
                    timeLimit = it.toLong(),
                    createdAt = Clock.System.now()
                )
            }.toList()
        )
        Log.d("SR-N", "setS 완료")

        delay(5000)

        getS()
    }

    fun getS() = viewModelScope.launch {
        subjectRepository.getSubjects().collectLatest {
            Log.d("SR-N", "getS : $it")
        }

        delay(5000)

        setE()
    }

    fun setE() = viewModelScope.launch {
        examRepository.insertExams(
            (1..20).map {
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

        delay(5000)

        getE()

    }

    fun getE() = viewModelScope.launch {
        examRepository.getExams().collectLatest {
            Log.d("SR-N", "getE : $it")
        }
    }
}
