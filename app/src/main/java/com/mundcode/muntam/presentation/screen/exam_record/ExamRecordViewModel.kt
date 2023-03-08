package com.mundcode.muntam.presentation.screen.exam_record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mundcode.domain.usecase.GetExamByIdFlowUseCase
import com.mundcode.domain.usecase.GetExamByIdUseCase
import com.mundcode.domain.usecase.GetSubjectByIdFlowUseCase
import com.mundcode.domain.usecase.GetSubjectByIdUseCase
import com.mundcode.domain.usecase.UpdateExamUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.navigation.ExamRecord
import com.mundcode.muntam.presentation.model.ExamModel
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
    getSubjectByIdUseCase: GetSubjectByIdUseCase,
    getSubjectByIdFlowUseCase: GetSubjectByIdFlowUseCase,
    getExamByIdUseCase: GetExamByIdUseCase,
    getExamByIdFlowUseCase: GetExamByIdFlowUseCase,
    updateExamUseCase: UpdateExamUseCase
) : BaseViewModel<ExamRecordState>() {
    private val subjectId: Int = checkNotNull(savedStateHandle[ExamRecord.subjectIdArg])
    private val examId: Int = checkNotNull(savedStateHandle[ExamRecord.examIdArg])

    lateinit var timer: ExamRecordTimer

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
        }
    }

    override fun createInitialState(): ExamRecordState {
        return ExamRecordState()
    }
}

data class ExamRecordState(
    val subjectModel: SubjectModel = SubjectModel(),
    val examModel: ExamModel = ExamModel(),
    val showBackConfirmDialog: Boolean = false,
    val showCompleteDialog: Boolean = false,
    val showJumpQuestionDialog: Boolean = false
)