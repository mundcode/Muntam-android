package com.mundcode.muntam.presentation.ui.main.subjects

import androidx.lifecycle.viewModelScope
import com.mundcode.domain.usecase.DeleteSubjectUseCase
import com.mundcode.domain.usecase.GetSubjectsUseCase
import com.mundcode.domain.usecase.InsertSubjectUseCase
import com.mundcode.domain.usecase.UpdateSubjectUseCase
import com.mundcode.muntam.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getSubjectsUseCase: GetSubjectsUseCase,
    private val insertSubjectUseCase: InsertSubjectUseCase,
    private val deleteSubjectUseCase: DeleteSubjectUseCase,
    private val updateSubjectUseCase: UpdateSubjectUseCase
) : BaseViewModel() {
    private val _subjects = MutableSharedFlow<List<SubjectState>>()
    val subjects: SharedFlow<List<SubjectState>> = _subjects

    init {
        getSubjects()
    }

    fun getSubjects() = viewModelScope.launch {
        getSubjectsUseCase().collectLatest { list ->
            _subjects.emit(list.map { it.asStateModel() })
        }
    }

    fun insertSubject() = viewModelScope.launch {

        insertSubjectUseCase(
            SubjectState(
                subjectTitle = "신참입니다."
            ).asExternalModel()
        )
    }

    fun updateSubject(subjectState: SubjectState) = viewModelScope.launch {
        updateSubjectUseCase(subjectState.copy(subjectTitle = "눌러짐").asExternalModel())
    }

    fun deleteSubject(subjectState: SubjectState) = viewModelScope.launch {
        deleteSubjectUseCase(subjectState.id)
    }
}
