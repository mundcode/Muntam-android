package com.mundcode.muntam.presentation.screen.questions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mundcode.domain.model.Question
import com.mundcode.domain.model.enums.QuestionSort
import com.mundcode.domain.usecase.GetExamByIdFlowUseCase
import com.mundcode.domain.usecase.GetQuestionsByExamIdFlowUseCase
import com.mundcode.domain.usecase.GetQuestionsByExamIdWithSortFlowUseCase
import com.mundcode.domain.usecase.UpdateQuestionUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.navigation.Questions
import com.mundcode.muntam.presentation.model.ExamModel
import com.mundcode.muntam.presentation.model.QuestionModel
import com.mundcode.muntam.presentation.model.asExternalModel
import com.mundcode.muntam.presentation.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapMerge
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

    fun onClickAlarm(questionsModel: QuestionModel) {
        // todo 워크 매니저 -> 에빙하우스 이론에 따라 보내기
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
}

data class QuestionsState(
    val exam: ExamModel = ExamModel(),
    val selectedSort: QuestionSort = QuestionSort.DEFAULT,
    val questions: List<QuestionModel> = listOf()
)