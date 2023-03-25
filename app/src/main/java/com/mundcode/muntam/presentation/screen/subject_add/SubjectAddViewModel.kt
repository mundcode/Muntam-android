package com.mundcode.muntam.presentation.screen.subject_add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundcode.domain.usecase.InsertSubjectUseCase
import com.mundcode.muntam.presentation.model.SubjectModel
import com.mundcode.muntam.presentation.model.asExternalModel
import com.mundcode.muntam.util.getRandomEmoji
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@HiltViewModel
class SubjectAddViewModel @Inject constructor(
    private val insertSubjectUseCase: InsertSubjectUseCase
) : ViewModel() {
    private val _subjectAddState = MutableStateFlow(SubjectAddState(emoji = getRandomEmoji()))
    val subjectAddState: StateFlow<SubjectAddState> = _subjectAddState

    val mutex = Mutex()

    fun onClickEmoji() {
        updateState {
            subjectAddState.value.copy(emoji = getRandomEmoji())
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
                subjectName = name.trim()
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
        Log.d("SR-N", "$hour / $min / $sec")
        updateState {
            subjectAddState.value.copy(
                showTimeLimitDialog = false,
                timeLimitHour = hour,
                timeLimitMin = min,
                timeLimitSec = sec,
                timeLimitText = if (hour == 0 && min == 0 && sec == 0) {
                    ""
                } else {
                    "%d시간 %02d분 %02d초".format(hour, min, sec)
                }
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
        if (subjectAddState.value.canEnableButton()) {
            val input = subjectAddState.value
            val subject = SubjectModel(
                subjectTitle = input.subjectName,
                emoji = input.emoji,
                timeLimit = input.getTimeLimitMilliSec(),
                totalQuestionNumber = input.totalQuestionNumber
            )
            insertSubjectUseCase(subject.asExternalModel())
            updateState {
                subjectAddState.value.copy(completeSubjectAddition = true)
            }
        }
    }

    private fun updateState(getNewState: () -> SubjectAddState) = viewModelScope.launch {
        mutex.withLock {
            val newState = getNewState()
            _subjectAddState.value = newState.copy(
                enableButton = newState.canEnableButton()
            )
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
    val completeSubjectAddition: Boolean = false,
    val timeLimitText: String = "",
    val enableButton: Boolean = false
) {
    fun getTimeLimitMilliSec(): Long =
        (timeLimitHour * 60 * 60).toLong() + (timeLimitMin * 60) + (timeLimitSec)

    fun canEnableButton(): Boolean =
        subjectName.isNotEmpty() && getTimeLimitMilliSec() != 0L && totalQuestionNumber in (1..200)
}
