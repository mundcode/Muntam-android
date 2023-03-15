package com.mundcode.muntam.presentation.screen.exam_record

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.domain.model.enums.QuestionState
import com.mundcode.domain.usecase.GetExamByIdFlowUseCase
import com.mundcode.domain.usecase.GetExamByIdUseCase
import com.mundcode.domain.usecase.GetQuestionsByExamIdFlowUseCase
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamRecordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSubjectByIdFlowUseCase: GetSubjectByIdFlowUseCase,
    private val getExamByIdUseCase: GetExamByIdUseCase,
    private val getExamByIdFlowUseCase: GetExamByIdFlowUseCase,
    private val updateExamUseCase: UpdateExamUseCase,
    private val getQuestionsByExamIdFlowUseCase: GetQuestionsByExamIdFlowUseCase,
    private val getQuestionsByExamIdUseCase: GetQuestionsByExamIdUseCase,
    private val updateQuestionUseCase: UpdateQuestionUseCase
) : BaseViewModel<ExamRecordState>() {
    private val subjectId: Int = checkNotNull(savedStateHandle[ExamRecord.subjectIdArg])
    private val examId: Int = checkNotNull(savedStateHandle[ExamRecord.examIdArg])

    lateinit var timer: ExamRecordTimer

    private val currentState: ExamState get() = state.value.examModel.state
    private val lastQuestionNumber: Int? get() = stateValue.examModel.lastQuestionNumber
    private val currentQuestion: QuestionModel?
        get() {
            val lastQuestionNumber = stateValue.examModel.lastQuestionNumber
            return stateValue.questionModels.find {
                it.questionNumber == lastQuestionNumber
            }
        }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val timeLimit = getSubjectByIdFlowUseCase(subjectId).firstOrNull()?.timeLimit ?: throw Exception()
            val initExam = getExamByIdUseCase(examId).asStateModel()
            Log.d("SR-N", "ExamRecordViewModel init exam : $initExam")
            val initQuestions = getQuestionsByExamIdUseCase(examId).map { it.asStateModel() }
            Log.d("SR-N", "ExamRecordViewModel init questions : $initQuestions")

            updateState {
                stateValue.copy(
                    examModel = initExam,
                    questionModels = initQuestions
                )
            }

            timer = ExamRecordTimer(
                initialTime = initExam.lastAt ?: 0,
                timeLimit = initExam.timeLimit,
                initQuestion = initQuestions,
            ) { current, remain ->
                updateState {
                    stateValue.copy(
                        currentExamTimeText = current,
                        remainExamTimeText = remain
                    )
                }
            }

            launch {
                getExamByIdFlowUseCase(examId).collectLatest {
                    Log.e("SR-N", "exam\nstate ${it.state} / lastQuestionNumber $lastQuestionNumber ")
                    updateState {
                        state.value.copy(examModel = it.asStateModel())
                    }
                }
            }


            launch {
                getQuestionsByExamIdFlowUseCase(examId).collectLatest {
                    Log.e("SR-N", "getQuestionsByExamIdFlowUseCase collectLatest size ${it.getOrNull((lastQuestionNumber ?: 1) - 1)?.questionNumber}")

                    updateState {
                        state.value.copy(
                            questionModels = it.map { it.asStateModel() }
                        )
                    }
                }
            }
        }
    }

    fun onClickScreen() = viewModelScope.launch(Dispatchers.IO) {
        Log.d("SR-N", "examModel : ${stateValue.examModel}")
        when (currentState) {
            ExamState.READY -> {
                Log.d("SR-N", "onClickScreen READY")
                timer.start()
                updateExamState(
                    newExamState = ExamState.RUNNING,
                    lastQuestionNumber = 1,
                    lastAt = timer.getCurrentTime()
                )
                updateQuestionState(1, QuestionState.RUNNING)
            }
            ExamState.PAUSE -> {
                timer.start()
                Log.d("SR-N", "onClickScreen PAUSE")

                updateExamState(newExamState = ExamState.RUNNING, lastAt = timer.getCurrentTime())
                updateQuestionState(stateValue.examModel.lastQuestionNumber, QuestionState.RUNNING)
            }
            ExamState.RUNNING -> {
                Log.d("SR-N", "onClickScreen RUNNING")

                val currentNumber = lastQuestionNumber ?: 1
                val currentQuestion = currentQuestion
                val currentQuestions = state.value.questionModels

                val nextQuestion: QuestionModel? = currentQuestions.find {
                    it.state != QuestionState.END
                            && it.state != QuestionState.PAUSE
                            && it.questionNumber > currentNumber // 현재번호보다 큰 번호 먼저
                } ?: currentQuestions.find {
                    it.state != QuestionState.END
                            && it.state != QuestionState.PAUSE
                            && it.questionNumber != currentNumber // 없으면 다른 것중 가장 앞
                }


                nextQuestion?.let { next ->// 풀 문제가 있다면
                    lapsAndUpdateQuestion(currentQuestion) // 현재 상태 문제 수정 및 기록

                    updateExamState(
                        newExamState = ExamState.RUNNING,
                        lastQuestionNumber = next.questionNumber,
                        lastAt = timer.getCurrentTime()
                    )

                    updateQuestionUseCase( // 다음 문제 상태 수정
                        next.copy(state = QuestionState.RUNNING).asExternalModel()
                    )
                } ?: run { // 풀 문제가 없다면 종료
                    end()
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
        pause()
    }

    fun onClickSetting() {
        // todo Setting 추가
    }

    fun onClickComplete() {
        updateState {
            stateValue.copy(
                showCompleteDialog = true
            )
        }
        pause()
    }

    fun onClickPause() {
        pause()
    }

    fun onClickJump() {
        updateState {
            stateValue.copy(
                showJumpQuestionDialog = true
            )
        }
        pause()
    }

    fun onSelectConfirmBackDialog() {
        onCancelDialog()
        pause()
        updateState {
            stateValue.copy(clickBack = true)
        }
    }

    fun onSelectConfirmCompleteDialog() {
        onCancelDialog()
        end()
    }

    fun onSelectNumberJumpDialog(selectedNumber: Int) = viewModelScope.launch(Dispatchers.IO) {
        onCancelDialog()
        lapsAndUpdateQuestion(currentQuestion)
        updateExamState(ExamState.RUNNING, lastQuestionNumber = selectedNumber)
        updateQuestionState(selectedNumber, newQuestionState = QuestionState.RUNNING)
    }

    private fun pause() = viewModelScope.launch(Dispatchers.IO) {
        timer.pause()
        updateExamState(newExamState = ExamState.PAUSE)
        lapsAndUpdateQuestion(currentQuestion)
    }

    private fun resume() = viewModelScope.launch(Dispatchers.IO) {
        timer.start()
        updateExamState(newExamState = ExamState.RUNNING)
        updateQuestionState(lastQuestionNumber, QuestionState.RUNNING)
    }

    private fun end() = viewModelScope.launch(Dispatchers.IO) {
        timer.end()
        updateExamState(ExamState.END)
        lapsAndUpdateQuestion(currentQuestion)
        stateValue.questionModels.forEach {
            updateQuestionUseCase(it.copy(state = QuestionState.END).asExternalModel())
        }
        updateState { stateValue.copy(completeAllQuestion = true) }
    }

    private fun onCancelDialog() {
        updateState {
            state.value.copy(
                showBackConfirmDialog = false,
                showCompleteDialog = false,
                showJumpQuestionDialog = false
            )
        }
    }

    private suspend fun updateExamState(
        newExamState: ExamState,
        lastQuestionNumber: Int? = null,
        lastAt: Long? = null
    ) {
        Log.d("SR-N", "updateExamState newExamState : $newExamState")
        updateExamUseCase(
            stateValue.examModel.copy(
                state = newExamState,
                lastQuestionNumber = lastQuestionNumber ?: stateValue.examModel.lastQuestionNumber,
                lastAt = lastAt ?: stateValue.examModel.lastAt
            ).asExternalModel()
        )
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

    override fun onCleared() {
        super.onCleared()
        timer.end()
    }
}

data class ExamRecordState(
    val timeLimit: Long = 0,
    val examModel: ExamModel = ExamModel(),
    val currentExamTimeText: String = "",
    val remainExamTimeText: String = "",
    val questionModels: List<QuestionModel> = listOf(),
    val showBackConfirmDialog: Boolean = false,
    val showCompleteDialog: Boolean = false,
    val showJumpQuestionDialog: Boolean = false,
    val completeAllQuestion: Boolean = false,
    val clickBack: Boolean = false
)