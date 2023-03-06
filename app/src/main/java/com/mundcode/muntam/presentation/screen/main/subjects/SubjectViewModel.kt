package com.mundcode.muntam.presentation.screen.main.subjects

import androidx.lifecycle.viewModelScope
import com.mundcode.domain.model.Subject
import com.mundcode.domain.usecase.DeleteSubjectUseCase
import com.mundcode.domain.usecase.GetSubjectsUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.presentation.model.SubjectModel
import com.mundcode.muntam.presentation.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getSubjectsUseCase: GetSubjectsUseCase,
    private val deleteSubjectUseCase: DeleteSubjectUseCase,
) : BaseViewModel<SubjectsState>() {
    init {
        loadSubjects()
    }

    private fun loadSubjects() = viewModelScope.launch(Dispatchers.IO) {
        getSubjectsUseCase().collectLatest { list ->
            updateState {
                // todo favorite 정렬
                state.value.copy(
                    subjects = list.map(Subject::asStateModel)
                )
            }
        }
    }

    private suspend fun deleteSubject(subjectModel: SubjectModel) =
        viewModelScope.launch(Dispatchers.IO) {
            deleteSubjectUseCase(subjectModel.id)
        }

    fun onClickDeleteSubject(subjectModel: SubjectModel) {
        updateState {
            state.value.copy(
                showDeleteConfirmDialog = true,
                selectedModel = subjectModel
            )
        }
    }

    fun onClickDeleteSubjectConfirm() = viewModelScope.launch {
        state.value.selectedModel?.let {
            deleteSubject(it)
            updateState {
                state.value.copy(
                    showDeleteConfirmDialog = false
                )
            }
        }
    }

    fun onCancelDialog() = updateState {
        state.value.copy(
            showDeleteConfirmDialog = false
        )
    }

    override fun createInitialState(): SubjectsState {
        return SubjectsState()
    }
}

data class SubjectsState(
    val subjects: List<SubjectModel> = listOf(),
    val selectedModel: SubjectModel? = null,
    val showDeleteConfirmDialog: Boolean = false
)
