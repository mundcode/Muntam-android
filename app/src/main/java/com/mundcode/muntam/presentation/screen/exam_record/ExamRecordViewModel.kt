package com.mundcode.muntam.presentation.screen.exam_record

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mundcode.designsystem.model.SelectableNumber
import com.mundcode.designsystem.model.SelectableTextState
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
import com.mundcode.muntam.navigation.Questions
import com.mundcode.muntam.presentation.model.ExamModel
import com.mundcode.muntam.presentation.model.QuestionModel
import com.mundcode.muntam.presentation.model.asExternalModel
import com.mundcode.muntam.presentation.model.asStateModel
import com.mundcode.muntam.presentation.screen.exam_record.ExamRecordTimer.Companion.DEFAULT_INITIAL_TIME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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

    private val currentExamState: ExamState get() = state.value.examModel.state
    private val lastQuestionNumber: Int? get() = stateValue.examModel.lastQuestionNumber
    private val currentQuestion: QuestionModel?
        get() {
            val lastQuestionNumber = stateValue.examModel.lastQuestionNumber
            return stateValue.questionModels.find {
                it.questionNumber == lastQuestionNumber
            }
        }

    init {
        // todo READY -> START 일 때 간헐적으로 타이머 작동안하는 버그 수정
        viewModelScope.launch(Dispatchers.IO) {
            // 초기 정보 가져오기
            val timeLimit = getSubjectByIdFlowUseCase(subjectId).firstOrNull()?.timeLimit ?: throw Exception()
            val initExam = getExamByIdUseCase(examId).asStateModel()
            val initQuestions = getQuestionsByExamIdUseCase(examId).map { it.asStateModel() }

            // 초기 정보로 상태 업데이트
            updateState {
                stateValue.copy(
                    timeLimit = timeLimit,
                    examModel = initExam,
                    questionModels = initQuestions
                )
            }

            // 초기 정보로 타이머 초기화
            timer = ExamRecordTimer(
                initialTime = initExam.lastAt ?: DEFAULT_INITIAL_TIME,
                timeLimit = initExam.timeLimit,
                initQuestion = initQuestions,
                initExamState = initExam.state
            ) { current, remain, question ->
                updateState {
                    stateValue.copy(
                        currentExamTimeText = current,
                        remainExamTimeText = remain,
                        currentQuestionTimeText = question,
                        expired = timer.getCurrentTime() > timeLimit / 1000,
                        percent = (timer.getCurrentTime() / (timeLimit / 1000).toFloat())
                    )
                }
            }

            // 시험, 문제 리스트 데이터 변화 구독
            combine(
                getExamByIdFlowUseCase(examId),
                getQuestionsByExamIdFlowUseCase(examId),
                ::Pair
            ).collectLatest { (examEntity, questionsEntity) ->

                val exam = examEntity.asStateModel()
                val questions = questionsEntity.map { it.asStateModel() }


                val lastQuestionNumber = exam.lastQuestionNumber
                val newQuestion = questions.find { q ->
                    q.questionNumber == lastQuestionNumber
                }

                Log.e("SR-N", "state ${examEntity.state} / curQ ${examEntity.lastQuestionNumber}")

                newQuestion?.let { new ->
                    timer.setCurrentQuestion(new)
                }

                when (exam.state) {
                    ExamState.PAUSE -> {
                        timer.pause()
                    }
                    ExamState.RUNNING -> {
                        timer.start()
                    }
                }

                updateState {
                    state.value.copy(
                        examModel = exam,
                        questionModels = questions
                    )
                }
            }
        }
    }

    fun onClickScreen() = viewModelScope.launch(Dispatchers.IO) {
        when (currentExamState) {
            ExamState.READY -> {
                Log.d("SR-N", "onClickScreen READY")
                updateQuestionState(
                    questionNumber = 1,
                    newQuestionState = QuestionState.RUNNING
                )
                updateExamState(
                    newExamState = ExamState.RUNNING,
                    lastQuestionNumber = 1,
                    lastAt = timer.getCurrentTime()
                )
            }
            ExamState.PAUSE -> {
                Log.d("SR-N", "onClickScreen PAUSE")
                resume()
            }
            ExamState.RUNNING -> {
                Log.d("SR-N", "onClickScreen RUNNING")

                val currentNumber = lastQuestionNumber
                    ?: throw Exception("RUNNING 상태에서 lastQuestionNumber 이 null 일 수 없음.")
                val currentQuestion = currentQuestion
                    ?: throw Exception("RUNNING 상태에서 currentQuestion 이 null 일 수 없음.")
                val currentQuestions = state.value.questionModels

                val nextQuestion: QuestionModel? = currentQuestions.find {
                    // 현재번호보다 큰 번호 먼저
                    it.state == QuestionState.READY && it.questionNumber > currentNumber
                } ?: currentQuestions.find {
                    // 없으면 다른 것중 가장 앞
                    it.state == QuestionState.READY && it.questionNumber != currentNumber
                }


                nextQuestion?.let { next ->// 풀 문제가 있다면
                    lapsAndPauseQuestion(currentQuestion) // 현재 상태 문제 수정 및 기록

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
            ExamState.END -> {
                // todo 광고 로직 삽입
                _navigationEvent.emit(Questions.route)
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
        updateState {
            stateValue.copy(
                showBackConfirmDialog = false,
                showCompleteDialog = false,
                showJumpQuestionDialog = false,
                confirmBack = true
            )
        }
    }

    fun onSelectConfirmCompleteDialog() {
        updateState {
            state.value.copy(
                showBackConfirmDialog = false,
                showCompleteDialog = false,
                showJumpQuestionDialog = false
            )
        }
        end()
    }

    fun onSelectNumberJumpDialog(selectedNumber: Int) = viewModelScope.launch(Dispatchers.IO) {
        updateState {
            state.value.copy(
                showBackConfirmDialog = false,
                showCompleteDialog = false,
                showJumpQuestionDialog = false
            )
        }
        lapsAndPauseQuestion(currentQuestion) // 기존 문제 PAUSE 시키고 기록하기
        updateExamState(ExamState.RUNNING, lastQuestionNumber = selectedNumber) // 타이머 리스타트
        updateQuestionState(selectedNumber, newQuestionState = QuestionState.RUNNING) // 선택문제 스타트
    }

    private fun pause() = viewModelScope.launch(Dispatchers.IO) {
        updateExamState(
            newExamState = ExamState.PAUSE,
            lastAt = timer.getCurrentTime()
        )
        lapsAndPauseQuestion(currentQuestion)
    }

    // 문제번호랑 경과시간은 그대로, 상태랑 타이머만 바꾸기
    private fun resume() = viewModelScope.launch(Dispatchers.IO) {
        updateExamState(newExamState = ExamState.RUNNING)
        updateQuestionState(lastQuestionNumber, QuestionState.RUNNING)
    }

    private fun end() = viewModelScope.launch(Dispatchers.IO) {
        // todo loading 상태 넣기?
        lapsAndPauseQuestion(currentQuestion)

        stateValue.questionModels.forEach {
            updateQuestionUseCase(it.copy(state = QuestionState.END).asExternalModel())
        }

        updateExamState(
            newExamState = ExamState.END,
            lastAt = timer.getCurrentTime()
        )
    }

    fun onCancelDialog() {
        updateState {
            state.value.copy(
                showBackConfirmDialog = false,
                showCompleteDialog = false,
                showJumpQuestionDialog = false
            )
        }
        resume()
    }

    private suspend fun updateExamState(
        newExamState: ExamState,
        lastQuestionNumber: Int? = null,
        lastAt: Long? = null
    ) {
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
                stateValue.questionModels.find {
                    it.questionNumber == last
                }?.let {
                    updateQuestionUseCase(
                        it.copy(state = newQuestionState).asExternalModel()
                    )
                }
            }
        }

    private suspend fun lapsAndPauseQuestion(current: QuestionModel?) {
        current?.let {
            updateQuestionUseCase(timer.addCompletedQuestion(current).asExternalModel())
        }
    }

    override fun createInitialState(): ExamRecordState {
        return ExamRecordState()
    }

    fun onDispose() {
        if (currentExamState == ExamState.RUNNING) {
            pause()
        }
    }

    override fun onCleared() {
        timer.end()
        super.onCleared()
    }
}

data class ExamRecordState(
    val timeLimit: Long = 0,
    val examModel: ExamModel = ExamModel(),
    val percent: Float = 0f,
    val expired: Boolean = false,
    val currentExamTimeText: String = "00:00:00",
    val remainExamTimeText: String = "00:00:00",
    val currentQuestionTimeText: String = "00:00:00",
    val questionModels: List<QuestionModel> = listOf(),
    val showBackConfirmDialog: Boolean = false,
    val showCompleteDialog: Boolean = false,
    val showJumpQuestionDialog: Boolean = false,
    val confirmBack: Boolean = false
) {
    val selectableNumbers = questionModels.map {
        SelectableNumber(
            number = it.questionNumber,
            state = when (it.state) {
                QuestionState.READY -> SelectableTextState.SELECTABLE
                QuestionState.RUNNING -> SelectableTextState.SELECTED
                else -> SelectableTextState.UNSELECTABLE
            }
        )
    }
}
