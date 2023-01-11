package com.mundcode.muntam

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundcode.domain.model.Subject
import com.mundcode.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
) : ViewModel() {


    fun getS() = viewModelScope.launch {
        subjectRepository.getSubjects().collectLatest {
            Log.d("SR-N", "list : $it")
        }
    }

    fun setS() = viewModelScope.launch {
        subjectRepository.insertSubjects(
            (10..20).map {
                Subject(
                    name = "과목명$it",
                    totalQuestionNumber = it,
                    timeLimit = it.toLong(),
                    createdAt = System.currentTimeMillis()
                )
            }.toList()
        )
    }
}
