package com.mundcode.muntam.presentation.screen.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundcode.domain.usecase.GetQuestionsByExamIdFlowUseCase
import com.mundcode.domain.usecase.InsertQuestionsExamUseCase
import com.mundcode.muntam.presentation.model.QuestionModel
import com.mundcode.muntam.presentation.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val insertQuestionsExamUseCase: InsertQuestionsExamUseCase,
    private val getQuestionsByExamIdFlowUseCase: GetQuestionsByExamIdFlowUseCase,
) : ViewModel() {
    // todo 여러 UseCase 모아서 화면에 필요한 모든 데이터 가져오는 상태모델 정의하고 교체
    private val _questions = MutableSharedFlow<List<QuestionModel>>()
    val questions: SharedFlow<List<QuestionModel>> = _questions

    fun insertQuestions(examId: Int, questionSize: Int) = viewModelScope.launch(Dispatchers.IO) {
        insertQuestionsExamUseCase(listOf())
    }

    fun getQuestions(examId: Int) = viewModelScope.launch(Dispatchers.IO) {
        getQuestionsByExamIdFlowUseCase(examId).collectLatest {
            _questions.emit(it.map { it.asStateModel() })
        }
    }
}
