package com.mundcode.muntam.presentation.screen.main.favorites

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.domain.usecase.DeleteExamUseCase
import com.mundcode.domain.usecase.GetFavoriteExamsUseCase
import com.mundcode.domain.usecase.UpdateExamUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.navigation.ExamRecord
import com.mundcode.muntam.navigation.Questions
import com.mundcode.muntam.navigation.SubjectModify
import com.mundcode.muntam.presentation.model.ExamModel
import com.mundcode.muntam.presentation.model.asExternalModel
import com.mundcode.muntam.presentation.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteExamViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFavoriteExamsUseCase: GetFavoriteExamsUseCase,
    private val updateExamUseCase: UpdateExamUseCase,
    private val deleteExamUseCase: DeleteExamUseCase
) : BaseViewModel<FavoriteExamState>() {
    private val subjectId: Int = checkNotNull(savedStateHandle[SubjectModify.subjectIdArg])

    init {
        loadExams()
    }

    private fun loadExams() = viewModelScope.launch(Dispatchers.IO) {
        getFavoriteExamsUseCase().collectLatest {
            updateState {
                state.value.copy(
                    exams = it.mapValues { (_, value) -> value.map { it.asStateModel() } }
                )
            }
        }
    }

    fun onClickExam(examModel: ExamModel) = viewModelScope.launch {
        if (examModel.state == ExamState.END && examModel.completeAd) {
            _navigationEvent.emit(Questions.route)
        } else {
            _navigationEvent.emit(ExamRecord.getRouteWithArgs(subjectId, examModel.id))
        }
    }

    fun onClickFavorite(examModel: ExamModel) = viewModelScope.launch(Dispatchers.IO) {
        updateExamUseCase(
            examModel.copy(
                isFavorite = examModel.isFavorite.not()
            ).asExternalModel()
        )
    }

    fun onClickMore(examModel: ExamModel) {
        updateState {
            stateValue.copy(
                focusedExam = examModel,
                showOptionBottomSheet = true
            )
        }
    }

    fun onSelectDeleteFromBottomSheet() {
        updateState {
            stateValue.copy(
                showOptionBottomSheet = false,
                showDeleteConfirmDialog = true
            )
        }
    }

    fun onSelectModifyFromBottomSheet() {
        updateState {
            stateValue.copy(
                showOptionBottomSheet = false,
                showModifyDialog = true
            )
        }
    }

    fun onSelectDeleteExam() = viewModelScope.launch(Dispatchers.IO) {
        onClearDialog()
        stateValue.focusedExam?.let {
            deleteExamUseCase(it.id)
        }
    }

    fun onModifyExamName(name: String) = viewModelScope.launch(Dispatchers.IO) {
        onClearDialog()
        stateValue.focusedExam?.let {
            updateExamUseCase(it.asExternalModel())
        }
    }


    fun onClearDialog() {
        updateState {
            state.value.copy(
                focusedExam = null,
                showOptionBottomSheet = false
            )
        }
    }

    override fun createInitialState(): FavoriteExamState {
        return FavoriteExamState()
    }
}

data class FavoriteExamState(
    val exams: Map<String, List<ExamModel>> = mapOf(),
    val focusedExam: ExamModel? = null,
    val showOptionBottomSheet: Boolean = false,
    val showModifyDialog: Boolean = false,
    val showDeleteConfirmDialog: Boolean = false
)