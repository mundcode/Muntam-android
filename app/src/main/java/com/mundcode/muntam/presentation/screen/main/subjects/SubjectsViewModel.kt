package com.mundcode.muntam.presentation.screen.main.subjects

import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.mundcode.domain.model.Subject
import com.mundcode.domain.usecase.DeleteSubjectUseCase
import com.mundcode.domain.usecase.GetQuestionsBySubjectIdUseCase
import com.mundcode.domain.usecase.GetSubjectsFlowUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.presentation.model.SubjectModel
import com.mundcode.muntam.presentation.model.asStateModel
import com.mundcode.muntam.worker.QuestionNotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectsViewModel @Inject constructor(
    private val getSubjectsFlowUseCase: GetSubjectsFlowUseCase,
    private val deleteSubjectUseCase: DeleteSubjectUseCase,
    private val getQuestionIdBySubjectIdUseCase: GetQuestionsBySubjectIdUseCase
) : BaseViewModel<SubjectsState>() {
    private val _alarmCancelEvent = MutableSharedFlow<String>()
    val alarmCancelEvent: SharedFlow<String> = _alarmCancelEvent

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

    fun onClickDeleteSubjectConfirm() = viewModelScope.launch(Dispatchers.IO) {

        state.value.selectedModel?.let { subject ->
            getQuestionIdBySubjectIdUseCase(subject.id).forEach { question -> // todo test
                if (question.isAlarm) {
                    _alarmCancelEvent.emit(
                        QuestionNotificationWorker.getWorkerIdWithArgs(questionId = question.id)
                    )
                }
            }
            deleteSubject(subject)
            onCancelDialog()
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
