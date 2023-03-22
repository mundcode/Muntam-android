package com.mundcode.muntam.presentation.screen.questions

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

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

    init {
        getExam()
        getQuestions()
    }

    @OptIn(FlowPreview::class)
    private fun getQuestions() = viewModelScope.launch(Dispatchers.IO) {
        currentSort.flatMapMerge { sort ->
            getQuestionsByExamIdWithSortFlowUseCase(examId, sort)
        }.collectLatest { questions ->
            updateState {
                stateValue.copy(questions = questions.map { it.asStateModel() })
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

    fun onClickAlarm(questionsModel: QuestionModel) = viewModelScope.launch(Dispatchers.IO) {
        val currentAlarmEnable = questionsModel.isAlarm

        updateQuestionUseCase(
            questionsModel.copy(isAlarm = currentAlarmEnable.not()).asExternalModel()
        )

        if (!currentAlarmEnable) {
            queueQuestionRemindNotification(
                id = questionsModel.id,
                howLong = "10분",
                duration = 10,
                unit = TimeUnit.MINUTES,
            )

            queueQuestionRemindNotification(
                id = questionsModel.id,
                howLong = "24시간",
                duration = 24,
                unit = TimeUnit.HOURS,
            )

            queueQuestionRemindNotification(
                id = questionsModel.id,
                howLong = "1주일",
                duration = 7,
                unit = TimeUnit.DAYS,
            )

            queueQuestionRemindNotification(
                id = questionsModel.id,
                howLong = "약 1달",
                duration = 30,
                unit = TimeUnit.DAYS,
            )
        } else {
            _alarmCancelEvent.emit(QuestionNotificationWorker.getWorkerIdWithArgs(questionId = questionsModel.id))
        }
        _toast.emit(if (currentAlarmEnable) "알람을 취소했어요." else "에빙하우스 망각곡선에 따라 알림을 설정했어요!")
    }

    fun onClickCorrect(questionsModel: QuestionModel) = viewModelScope.launch(Dispatchers.IO) {
        updateQuestionUseCase(
            questionsModel.copy(isCorrect = questionsModel.isCorrect.not()).asExternalModel()
        )
    }

    fun onClickSortLapsDesc() {
        updateState {
            stateValue.copy(selectedSort = QuestionSort.LAPS_DESC)
        }
        _currentSort.value = QuestionSort.LAPS_DESC
    }

    fun onClickSortWrongFirst() {
        updateState {
            stateValue.copy(selectedSort = QuestionSort.WRONG_FIRST)
        }
        _currentSort.value = QuestionSort.WRONG_FIRST
    }

    fun onClickSortNumberAsc() {
        updateState {
            stateValue.copy(selectedSort = QuestionSort.DEFAULT)
        }
        _currentSort.value = QuestionSort.DEFAULT
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
    val selectedSort: QuestionSort = QuestionSort.DEFAULT,
    val questions: List<QuestionModel> = listOf()
)
