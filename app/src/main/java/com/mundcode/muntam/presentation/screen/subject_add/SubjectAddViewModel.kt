package com.mundcode.muntam.presentation.screen.subject_add

import androidx.lifecycle.viewModelScope
import com.mundcode.domain.usecase.UpdateSubjectUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.presentation.model.SubjectModel
import com.mundcode.muntam.presentation.model.asExternalModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class SubjectAddViewModel @Inject constructor(
    private val updateSubjectUseCase: UpdateSubjectUseCase
) : BaseViewModel() {
    private val emojiList = listOf(
        "💎",
        "⏰",
        "🥰"
    ) // todo 데이터베이스에 대량으로 넣고 가져오기

    private val _subjectAddState = MutableStateFlow(SubjectAddState(emoji = emojiList.random()))
    val subjectAddState: StateFlow<SubjectAddState> = _subjectAddState

    val mutex = Mutex()

    fun onClickEmoji() {
        updateState {
            subjectAddState.value.copy(emoji = emojiList.random())
        }
    }

    fun onClickSubjectName() {
        updateState {
            subjectAddState.value.copy(showNameDialog = true)
        }
    }

    fun onSelectSubjectName(name: String) {
        updateState {
            subjectAddState.value.copy(
                showNameDialog = false,
                subjectName = name
            )
        }
    }

    fun onClickTimeLimit() {
        updateState {
            subjectAddState.value.copy(
                showTimeLimitDialog = true
            )
        }
    }

    fun onSelectTimeLimit(hour: Int, min: Int, sec: Int) {
        updateState {
            subjectAddState.value.copy(
                showTimeLimitDialog = false,
                timeLimitHour = hour,
                timeLimitMin = min,
                timeLimitSec = sec
            )
        }
    }

    fun onClickTotalQuestionNumber() {
        updateState {
            subjectAddState.value.copy(
                showTotalQuestionNumberDialog = true
            )
        }
    }

    fun onSelectTotalQuestionNumber(totalQuestionNumber: Int) {
        updateState {
            subjectAddState.value.copy(
                showTotalQuestionNumberDialog = false,
                totalQuestionNumber = totalQuestionNumber
            )
        }
    }

    fun onCancelDialog() {
        updateState {
            subjectAddState.value.copy(
                showNameDialog = false,
                showTimeLimitDialog = false,
                showTotalQuestionNumberDialog = false
            )
        }
    }

    fun onClickCompleteButton() = viewModelScope.launch(Dispatchers.IO) {
        if (subjectAddState.value.enableButton) {
            val input = subjectAddState.value
            val subject = SubjectModel(
                subjectTitle = input.subjectName ?: return@launch,
                emoji = input.emoji,
                timeLimit = input.timeLimit ?: return@launch,
                totalQuestionNumber = input.totalQuestionNumber ?: return@launch
            )
            updateSubjectUseCase(subject.asExternalModel())
            updateState {
                subjectAddState.value.copy(completeSubjectAddition = true)
            }
        }
    }


    private fun updateState(newState: () -> SubjectAddState) = viewModelScope.launch {
        mutex.withLock {
            _subjectAddState.value = newState()
        }
    }
}

data class SubjectAddState(
    val emoji: String = "⏰",
    val subjectName: String = "",
    val timeLimitHour: Int = 0,
    val timeLimitMin: Int = 0,
    val timeLimitSec: Int = 0,
    val totalQuestionNumber: Int = 0,
    val showNameDialog: Boolean = false,
    val showTimeLimitDialog: Boolean = false,
    val showTotalQuestionNumberDialog: Boolean = false,
    val completeSubjectAddition: Boolean = false
) {

    val timeLimitText get() =  if (timeLimitHour == 0 && timeLimitMin == 0 && timeLimitSec == 0) {
        ""
    } else {
        "%d시간 %02d분 %02d초".format(timeLimitHour, timeLimitMin, timeLimitSec)
    }

    val timeLimit get() =
        (timeLimitHour * 60 * 60 * 1000L) + (timeLimitMin * 60 * 1000L) + (timeLimitSec * 1000L)

    val enableButton =
        subjectName.isNotEmpty() && timeLimit != 0L && totalQuestionNumber in (1..200)

}

