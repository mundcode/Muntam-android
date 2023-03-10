package com.mundcode.muntam.presentation.screen.exam_record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.domain.model.enums.QuestionState
import com.mundcode.domain.usecase.GetExamByIdFlowUseCase
import com.mundcode.domain.usecase.GetExamByIdUseCase
import com.mundcode.domain.usecase.GetQuestionsByExamIdUseCase
import com.mundcode.domain.usecase.GetSubjectByIdFlowUseCase
import com.mundcode.domain.usecase.UpdateExamUseCase
import com.mundcode.domain.usecase.UpdateQuestionUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.navigation.ExamRecord
import com.mundcode.muntam.presentation.model.ExamModel
import com.mundcode.muntam.presentation.model.QuestionModel
import com.mundcode.muntam.presentation.model.SubjectModel
import com.mundcode.muntam.presentation.model.asExternalModel
import com.mundcode.muntam.presentation.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamRecordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSubjectByIdFlowUseCase: GetSubjectByIdFlowUseCase,
    private val getExamByIdUseCase: GetExamByIdUseCase,
    private val getExamByIdFlowUseCase: GetExamByIdFlowUseCase,
    private val updateExamUseCase: UpdateExamUseCase,
    private val getQuestionsByExamIdUseCase: GetQuestionsByExamIdUseCase,
    private val updateQuestionUseCase: UpdateQuestionUseCase
) : BaseViewModel<ExamRecordState>() {
    private val subjectId: Int = checkNotNull(savedStateHandle[ExamRecord.subjectIdArg])
    private val examId: Int = checkNotNull(savedStateHandle[ExamRecord.examIdArg])

    lateinit var timer: ExamRecordTimer

    private val currentState: ExamState get() = state.value.examModel.state

    init {
        viewModelScope.launch {
            val exam = getExamByIdUseCase(examId).asStateModel()

            timer = ExamRecordTimer(
                initialTime = exam.lastAt ?: 0,
                timeLimit = exam.timeLimit,
                scope = viewModelScope
            ) { sec: Long ->
                updateExamUseCase(exam.copy(lastAt = sec).asExternalModel())
            }

            getSubjectByIdFlowUseCase(subjectId).collectLatest {
                updateState {
                    state.value.copy(subjectModel = it.asStateModel())
                }
            }

            getExamByIdFlowUseCase(examId).collectLatest {
                updateState {
                    when (it.state) {
                        ExamState.RUNNING -> {
                            timer.start()
                        }
                        else -> {
                            timer.pause()
                        }
                    }
                    state.value.copy(examModel = it.asStateModel())
                }
            }

            getQuestionsByExamIdUseCase(examId).collectLatest {
                updateState {
                    state.value.copy(
                        questionModels = it.map { it.asStateModel() }
                    )
                }
            }
        }
    }

    fun onClickScreen() = viewModelScope.launch(Dispatchers.IO) {
        when (currentState) {
            ExamState.READY -> {
                updateExamState(newExamState = ExamState.RUNNING, lastQuestionNumber = 1)
                updateQuestionState(1, QuestionState.RUNNING)
            }
            ExamState.PAUSE -> {
                updateExamState(ExamState.RUNNING)
                updateQuestionState(stateValue.examModel.lastQuestionNumber, QuestionState.RUNNING)
            }
            ExamState.RUNNING -> {
                val currentNumber = state.value.examModel.lastQuestionNumber
                val currentQuestion = currentNumber?.let {
                    stateValue.questionModels.getOrNull(it)
                }
                val nextQuestion: QuestionModel? =
                    if (currentNumber != null && currentNumber + 1 < state.value.subjectModel.totalQuestionNumber) {
                        stateValue.questionModels.getOrNull(currentNumber + 1)
                    } else {
                        val currentQuestions = state.value.questionModels
                        currentQuestions.find {
                            it.state != QuestionState.END
                                    && it.state != QuestionState.PAUSE
                                    && it.questionNumber != currentNumber
                        }
                    }

                nextQuestion?.let { next ->
                    updateExamUseCase(
                        state.value.examModel.copy(
                            lastQuestionNumber = next.questionNumber
                        ).asExternalModel()
                    )

                    updateQuestionUseCase(
                        next.copy(state = QuestionState.RUNNING).asExternalModel()
                    )
                    lapsAndUpdateQuestion(currentQuestion)
                } ?: run {
                    updateExamState(ExamState.END)
                    lapsAndUpdateQuestion(currentQuestion)
                    stateValue.questionModels.forEach {
                        updateQuestionUseCase(it.copy(state = QuestionState.END).asExternalModel())
                    }
                    updateState { stateValue.copy(completeAllQuestion = true) }
                }
            }
        }
    }

    fun onClickBack() = viewModelScope.launch {
        updateState {
            stateValue.copy(
                showBackConfirmDialog = true
            )
        }
    }

    fun onClickComplete() {
        updateState {
            stateValue.copy(
                showCompleteDialog = true
            )
        }
    }

    fun onClickJump() {
        updateState {
            stateValue.copy(
                showJumpQuestionDialog = true
            )
        }
    }

    fun onClickPause() = viewModelScope.launch(Dispatchers.IO) {
        updateExamUseCase(
            stateValue.examModel.copy(
                state = ExamState.PAUSE
            ).asExternalModel()
        )


    }


    fun onSelectConfirmBackDialog() {

    }

    fun onSelectConfirmCompleteDialog() {

    }

    fun onSelectNumberJumpDialog() {

    }

    fun onCancelDialog() {
        updateState {
            state.value.copy(
                showBackConfirmDialog = false,
                showCompleteDialog = false,
                showJumpQuestionDialog = false
            )
        }
    }

    private suspend fun updateExamState(newExamState: ExamState, lastQuestionNumber: Int? = null) {
        lastQuestionNumber?.let {
            updateExamUseCase(
                state.value.examModel.copy(
                    state = newExamState,
                    lastQuestionNumber = it
                ).asExternalModel()
            )
        } ?: run {
            updateExamUseCase(
                state.value.examModel.copy(
                    state = newExamState
                ).asExternalModel()
            )
        }
    }

    private suspend fun updateQuestionState(questionNumber: Int?, newQuestionState: QuestionState) =
        viewModelScope.launch {
            questionNumber?.let { last ->
                stateValue.questionModels.find { it.questionNumber == last }?.let {
                    updateQuestionUseCase(
                        it.copy(state = newQuestionState).asExternalModel()
                    )
                }
            }
        }

    private suspend fun lapsAndUpdateQuestion(question: QuestionModel?) {
        question?.let {
            updateQuestionUseCase(timer.addCompletedQuestion(it).asExternalModel())
        }
    }

    override fun createInitialState(): ExamRecordState {
        return ExamRecordState()
    }
}

data class ExamRecordState(
    val subjectModel: SubjectModel = SubjectModel(),
    val examModel: ExamModel = ExamModel(),
    val questionModels: List<QuestionModel> = listOf(),
    val showBackConfirmDialog: Boolean = false,
    val showCompleteDialog: Boolean = false,
    val showJumpQuestionDialog: Boolean = false,
    val completeAllQuestion: Boolean = false
)