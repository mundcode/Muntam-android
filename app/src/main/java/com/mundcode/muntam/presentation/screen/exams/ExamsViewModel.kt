package com.mundcode.muntam.presentation.screen.exams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundcode.domain.usecase.DeleteExamUseCase
import com.mundcode.domain.usecase.GetExamsUseCase
import com.mundcode.domain.usecase.InsertExamUseCase
import com.mundcode.domain.usecase.UpdateExamUseCase
import com.mundcode.muntam.presentation.model.ExamModel
import com.mundcode.muntam.presentation.model.asExternalModel
import com.mundcode.muntam.presentation.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class ExamsViewModel @Inject constructor(
    private val getExamsUseCase: GetExamsUseCase,
    private val insertExamUseCase: InsertExamUseCase,
    private val deleteExamUseCase: DeleteExamUseCase,
    private val updateExamUseCase: UpdateExamUseCase
) : ViewModel() {
    private val _exams = MutableSharedFlow<List<ExamModel>>()
    val exams: SharedFlow<List<ExamModel>> = _exams

    fun getExams(subjectId: Int) = viewModelScope.launch {
        getExamsUseCase(subjectId).collectLatest { list ->
            _exams.emit(list.map { it.asStateModel() })
        }
    }

    // todo https://stackoverflow.com/questions/29341380/sqlite-foreign-key-constraint-failed-code-787
    fun insertExam(subjectId: Int) = viewModelScope.launch {
        insertExamUseCase(
            ExamModel(
                subjectId = subjectId,
                name = "신참이다",
                createdAt = Clock.System.now(),
                timeLimit = subjectId * 100000L
            ).asExternalModel()
        )
    }

    fun updateExam(examModel: ExamModel) = viewModelScope.launch {
        updateExamUseCase(examModel.copy(name = "눌러짐").asExternalModel())
    }

    fun deleteExam(examModel: ExamModel) = viewModelScope.launch {
        deleteExamUseCase(examModel.id)
    }
}
