package com.mundcode.muntam.presentation.ui.main.subjects

import androidx.lifecycle.viewModelScope
import com.mundcode.domain.usecase.DeleteSubjectUseCase
import com.mundcode.domain.usecase.GetSubjectsUseCase
import com.mundcode.domain.usecase.InsertSubjectUseCase
import com.mundcode.domain.usecase.UpdateSubjectUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.presentation.ui.model.SubjectModel
import com.mundcode.muntam.presentation.ui.model.asExternalModel
import com.mundcode.muntam.presentation.ui.model.asStateModel
import com.mundcode.muntam.presentation.ui.model.createMockedSubjectModel
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
    private val _subjects = MutableSharedFlow<List<SubjectModel>>()
    val subjects: SharedFlow<List<SubjectModel>> = _subjects

    init {
        getSubjects()
    }

    private fun getSubjects() = viewModelScope.launch {
        getSubjectsUseCase().collectLatest { list ->
            _subjects.emit(list.map { it.asStateModel() })
        }
    }

    fun insertSubject() = viewModelScope.launch {
        insertSubjectUseCase(
            createMockedSubjectModel(1).asExternalModel()
        )
    }

    fun updateSubject(subjectModel: SubjectModel) = viewModelScope.launch {
        updateSubjectUseCase(subjectModel.copy(subjectTitle = "눌러짐").asExternalModel())
    }

    fun deleteSubject(subjectModel: SubjectModel) = viewModelScope.launch {
        deleteSubjectUseCase(subjectModel.id)
    }
}
