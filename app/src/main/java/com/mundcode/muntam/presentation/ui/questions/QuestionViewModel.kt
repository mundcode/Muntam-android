package com.mundcode.muntam.presentation.ui.questions

import androidx.lifecycle.viewModelScope
import com.mundcode.domain.usecase.GetExamByIdUseCase
import com.mundcode.domain.usecase.GetExamUseCase
import com.mundcode.domain.usecase.GetExamsUseCase
import com.mundcode.domain.usecase.GetQuestionByExamUseCase
import com.mundcode.domain.usecase.GetQuestionsUseCase
import com.mundcode.domain.usecase.InsertQuestionsExamUseCase
import com.mundcode.domain.usecase.UpdateQuestionUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.presentation.ui.model.QuestionState
import com.mundcode.muntam.presentation.ui.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getExamUseCase: GetExamUseCase,
    private val insertQuestionsExamUseCase: InsertQuestionsExamUseCase,
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val getQuestionByIdUseCase: GetExamByIdUseCase,
    private val getQuestionByExamUseCase: GetQuestionByExamUseCase,
    private val updateQuestionUseCase: UpdateQuestionUseCase
) : BaseViewModel() {
    private val _questions = MutableSharedFlow<List<QuestionState>>() // todo 여러 UseCase 모아서 화면에 필요한 모든 데이터 가져오는 상태모델 정의하고 교체
    val questions: SharedFlow<List<QuestionState>> = _questions

    fun insertQuestions(examId: Int, questionSize: Int) = viewModelScope.launch(Dispatchers.IO) {
        insertQuestionsExamUseCase(listOf(

        ))
    }

    fun getQuestions(examId: Int) = viewModelScope.launch(Dispatchers.IO) {
        getQuestionsUseCase(examId).collectLatest {
            _questions.emit(it.map { it.asStateModel() })
        }
    }
}