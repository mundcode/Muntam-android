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
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.navigation.ExamRecord
import com.mundcode.muntam.presentation.model.ExamModel
import com.mundcode.muntam.presentation.model.QuestionModel
import com.mundcode.muntam.presentation.model.SubjectModel
import com.mundcode.muntam.presentation.model.asExternalModel
import com.mundcode.muntam.presentation.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val getQuestionsByExamIdUseCase: GetQuestionsByExamIdUseCase
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

    fun onClickScreen() {
        when (currentState) {
            ExamState.READY -> {
                timer.start()
            }
            ExamState.PAUSE -> {
                timer.start()
            }
            ExamState.RUNNING -> {
                val currentNumber = (state.value.examModel.lastQuestionNumber ?: 1)
                val nextNumber: Int? =
                    if (currentNumber + 1 < state.value.subjectModel.totalQuestionNumber) {
                        currentNumber + 1
                    } else {
                        val currentQuestions = state.value.questionModels
                        currentQuestions.find { it.state != QuestionState.END }?.questionNumber?.let {
                            it
                        }
                    }

                viewModelScope.launch {
                    nextNumber?.let {
                        updateExamUseCase(
                            state.value.examModel.copy(
                                lastQuestionNumber = it
                            ).asExternalModel()
                        )
                    } ?: run {
                        updateExamUseCase(
                            state.value.examModel.copy(
                                state = ExamState.END
                            ).asExternalModel()
                        )
                    }
                }
            }
        }
    }

    private fun onClickStart() {

    }

    fun onClickBack() {

    }

    fun onClickComplete() {

    }

    fun onClickPause() {

    }

    fun onClickJump() {

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
    val showJumpQuestionDialog: Boolean = false
)