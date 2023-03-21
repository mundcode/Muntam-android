package com.mundcode.muntam.presentation.screen.subject_modify

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mundcode.domain.usecase.GetSubjectByIdUseCase
import com.mundcode.domain.usecase.UpdateSubjectUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.navigation.SubjectModify
import com.mundcode.muntam.presentation.model.SubjectModel
import com.mundcode.muntam.presentation.model.asExternalModel
import com.mundcode.muntam.presentation.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class SubjectModifyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSubjectByIdUseCase: GetSubjectByIdUseCase,
    private val updateSubjectUseCase: UpdateSubjectUseCase
) : BaseViewModel<SubjectModifyState>() {
    private val subjectId: Int = checkNotNull(savedStateHandle[SubjectModify.subjectIdArg])

    private val emojiList = listOf( // todo 데이터베이스에 대량으로 넣고 가져오기
        "💎",
        "⏰",
        "🥰",
        "👁",
        "🦾",
        "👅",
        "🧠"
    )

    init {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val subject = getSubjectByIdUseCase(subjectId.toInt())
                updateState {
                    state.value.copy(
                        subjectModel = subject.asStateModel(),
                        changedEmoji = subject.emoji,
                        changedName = subject.name
                    )
                }
            }
        } catch (e: Exception) {
            Log.d("SR-N", "SubjectModifyViewModel $e")
        }
    }

    fun onClickEmoji() = updateState {
        state.value.copy(
            changedEmoji = emojiList.random()
        )
    }

    fun onClickName() = updateState {
        state.value.copy(
            showNameEditorDialog = true
        )
    }

    fun onSelectName(name: String) = updateState {
        state.value.copy(
            showNameEditorDialog = false,
            changedName = name
        )
    }

    fun onCancelDialog() = updateState {
        state.value.copy(
            showNameEditorDialog = false
        )
    }

    fun onClickComplete() = viewModelScope.launch(Dispatchers.IO) {
        updateSubjectUseCase.invoke(
            state.value.subjectModel.copy(
                subjectTitle = state.value.changedName,
                emoji = state.value.changedEmoji
            ).asExternalModel()
        )

        updateState {
            state.value.copy(
                onCompleteUpdate = true
            )
        }
    }

    override fun createInitialState(): SubjectModifyState {
        return SubjectModifyState()
    }
}

data class SubjectModifyState(
    val subjectModel: SubjectModel = SubjectModel(),
    val changedName: String = "",
    val changedEmoji: String = "",
    val showNameEditorDialog: Boolean = false,
    val onCompleteUpdate: Boolean = false
)
