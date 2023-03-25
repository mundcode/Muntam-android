package com.mundcode.muntam.presentation.screen.questions

import android.os.Build
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import com.mundcode.domain.model.enums.QuestionSort
import com.mundcode.domain.usecase.GetExamByIdFlowUseCase
import com.mundcode.domain.usecase.GetQuestionsByExamIdWithSortFlowUseCase
import com.mundcode.domain.usecase.UpdateQuestionUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.navigation.Questions
import com.mundcode.muntam.presentation.model.ExamModel
import com.mundcode.muntam.presentation.model.QuestionModel
import com.mundcode.muntam.presentation.model.asExternalModel
import com.mundcode.muntam.presentation.model.asStateModel
import com.mundcode.muntam.worker.QuestionNotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQuestionsByExamIdWithSortFlowUseCase: GetQuestionsByExamIdWithSortFlowUseCase,
    private val getExamByIdFlowUseCase: GetExamByIdFlowUseCase,
    private val updateQuestionUseCase: UpdateQuestionUseCase
) : BaseViewModel<QuestionsState>() {
    private val examId: Int = checkNotNull(savedStateHandle[Questions.examIdArg])

    private val _currentSort = MutableStateFlow(QuestionSort.DEFAULT)
    val currentSort: StateFlow<QuestionSort> = _currentSort.asStateFlow()

    private val _alarmRequestEvent = MutableSharedFlow<OneTimeWorkRequest>()
    val alarmRequestEvent: SharedFlow<OneTimeWorkRequest> = _alarmRequestEvent

    private val _alarmCancelEvent = MutableSharedFlow<String>()
    val alarmCancelEvent: SharedFlow<String> = _alarmCancelEvent

    private val _notificationPermissionEvent = MutableSharedFlow<Unit>()
    val notificationPermissionEvent: SharedFlow<Unit> =
        _notificationPermissionEvent.asSharedFlow()

    init {
        getExam()
        getQuestions()
    }

    private fun getQuestions() = viewModelScope.launch(Dispatchers.IO) {
        // 여기서 flatMapMerge 를 쓰면안됨.
        currentSort.collectLatest {
            getQuestionsByExamIdWithSortFlowUseCase(
                examId,
                currentSort.value
            ).collectLatest { questions ->
                updateState {
                    stateValue.copy(questions = questions.map { it.asStateModel() })
                }
            }
        }
    }

    private fun getExam() = viewModelScope.launch(Dispatchers.IO) {
        getExamByIdFlowUseCase.invoke(examId).collectLatest {
            updateState {
                stateValue.copy(
                    exam = it.asStateModel()
                )
            }
        }
    }

    fun onClickAlarm(
        questionModel: QuestionModel,
        isGranted: Boolean,
        shouldShowRationale: Boolean
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (isGranted.not()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                showRationale(questionModel = questionModel)
            } else {
                updateState {
                    stateValue.copy(
                        pendingQuestion = questionModel
                    )
                }
                _notificationPermissionEvent.emit(Unit)
            }
            return@launch
        }

        if (shouldShowRationale) {
            showRationale(questionModel = questionModel)
            return@launch
        }

        enqueueQuestionNotification(questionModel = questionModel)
    }

    fun onPermissionGranted() = viewModelScope.launch(Dispatchers.IO) {
        val questionModel = stateValue.pendingQuestion ?: return@launch
        enqueueQuestionNotification(questionModel)
    }

    fun checkOnResume(isGranted: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        val questionModel = stateValue.pendingQuestion ?: return@launch
        if (!isGranted) return@launch

        enqueueQuestionNotification(questionModel)
    }

    fun showRationale(questionModel: QuestionModel? = null) {
        updateState {
            stateValue.copy(
                pendingQuestion = questionModel,
                shouldShowRationale = true
            )
        }
    }

    private suspend fun enqueueQuestionNotification(
        questionModel: QuestionModel,
    ) {
        val currentAlarmEnable = questionModel.isAlarm

        if (!currentAlarmEnable) {
            queueQuestionRemindNotification(
                id = questionModel.id,
                howLong = "10분",
                duration = 10,
                unit = TimeUnit.MINUTES,
            )

            queueQuestionRemindNotification(
                id = questionModel.id,
                howLong = "24시간",
                duration = 24,
                unit = TimeUnit.HOURS,
            )

            queueQuestionRemindNotification(
                id = questionModel.id,
                howLong = "1주일",
                duration = 7,
                unit = TimeUnit.DAYS,
            )

            queueQuestionRemindNotification(
                id = questionModel.id,
                howLong = "약 1달",
                duration = 30,
                unit = TimeUnit.DAYS,
            )
        } else {
            _alarmCancelEvent.emit(
                QuestionNotificationWorker.getWorkerIdWithArgs(questionId = questionModel.id)
            )
        }

        updateQuestionUseCase(
            questionModel.copy(
                isAlarm = currentAlarmEnable.not()
            ).asExternalModel()
        )

        updateState {
            stateValue.copy(
                pendingQuestion = null
            )
        }

        _toast.emit(if (currentAlarmEnable) "알람을 취소했어요." else "에빙하우스 망각곡선에 따라 알림을 설정했어요!")
    }

    fun onClickCorrect(questionsModel: QuestionModel) = viewModelScope.launch(Dispatchers.IO) {
        updateQuestionUseCase(
            questionsModel.copy(isCorrect = questionsModel.isCorrect.not()).asExternalModel()
        )
    }

    fun onClickSortLapsDesc() {
        _currentSort.value = QuestionSort.LAPS_DESC
    }

    fun onClickSortWrongFirst() {
        _currentSort.value = QuestionSort.WRONG_FIRST
    }

    fun onClickSortNumberAsc() {
        _currentSort.value = QuestionSort.DEFAULT
    }

    fun onCancelDialog() {
        updateState {
            stateValue.copy(
                shouldShowRationale = false
            )
        }
    }

    override fun createInitialState(): QuestionsState {
        return QuestionsState()
    }

    private suspend fun queueQuestionRemindNotification(
        id: Int,
        howLong: String,
        duration: Long,
        unit: TimeUnit
    ) {
        _alarmRequestEvent.emit(
            OneTimeWorkRequestBuilder<QuestionNotificationWorker>()
                .setInputData(
                    Data.Builder()
                        .putAll(
                            mapOf(
                                QuestionNotificationWorker.PARAM_QUESTION_ID to id,
                                QuestionNotificationWorker.PARAM_HOW_LONG to howLong
                            )
                        )
                        .build()
                )
                .addTag(QuestionNotificationWorker.getWorkerIdWithArgs(questionId = id))
                .setInitialDelay(duration, unit)
                .build()
        )
    }
}

data class QuestionsState(
    val exam: ExamModel = ExamModel(),
    val questions: List<QuestionModel> = listOf(),
    val pendingQuestion: QuestionModel? = null,
    val shouldShowRationale: Boolean = false
)
