package com.mundcode.muntam.presentation.screen.main.subjects

import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.mundcode.domain.model.Subject
import com.mundcode.domain.usecase.DeleteSubjectUseCase
import com.mundcode.domain.usecase.GetSubjectsFlowUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.presentation.model.SubjectModel
import com.mundcode.muntam.presentation.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getSubjectsFlowUseCase: GetSubjectsFlowUseCase,
    private val deleteSubjectUseCase: DeleteSubjectUseCase,
) : BaseViewModel<SubjectsState>() {
    init {
        loadSubjects()
        checkNoticeDialog()
    }

    private fun loadSubjects() = viewModelScope.launch(Dispatchers.IO) {
        getSubjectsFlowUseCase().collectLatest { list ->
            updateState {
                state.value.copy(
                    subjects = list.map(Subject::asStateModel)
                )
            }
        }
    }

    private fun checkNoticeDialog() = viewModelScope.launch(Dispatchers.IO) {
        // todo data 레이어로 이동
        Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener {
            val notice = Firebase.remoteConfig.getString("notice")
            if (notice.isNotEmpty()) {
                updateState {
                    stateValue.copy(
                        showNoticeDialog = true,
                        noticeText = notice
                    )
                }
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
            showDeleteConfirmDialog = false,
            showNoticeDialog = false
        )
    }

    override fun createInitialState(): SubjectsState {
        return SubjectsState()
    }
}

data class SubjectsState(
    val subjects: List<SubjectModel> = listOf(),
    val selectedModel: SubjectModel? = null,
    val showDeleteConfirmDialog: Boolean = false,
    val showNoticeDialog: Boolean = false,
    val noticeText: String = ""
)
