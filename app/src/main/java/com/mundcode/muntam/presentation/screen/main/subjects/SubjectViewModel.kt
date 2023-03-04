package com.mundcode.muntam.presentation.screen.main.subjects

import androidx.lifecycle.viewModelScope
import com.mundcode.domain.usecase.DeleteSubjectUseCase
import com.mundcode.domain.usecase.GetSubjectsUseCase
import com.mundcode.domain.usecase.UpdateSubjectUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.presentation.model.SubjectModel
import com.mundcode.muntam.presentation.model.asStateModel
import com.mundcode.muntam.presentation.model.createMockedSubjectModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getSubjectsUseCase: GetSubjectsUseCase,
    private val deleteSubjectUseCase: DeleteSubjectUseCase,
) : BaseViewModel() {
    private val _subjects = MutableStateFlow<List<SubjectModel>>(listOf())
    val subjects: StateFlow<List<SubjectModel>> = _subjects.asStateFlow()

    init {
        getSubjects()
    }

    private fun getSubjects() = viewModelScope.launch(Dispatchers.IO) {
        _subjects.emit(createMockedSubjectModels(12))
        getSubjectsUseCase().collectLatest { list ->
            _subjects.emit(list.map { it.asStateModel() })
        }
    }

    fun deleteSubject(subjectModel: SubjectModel) = viewModelScope.launch(Dispatchers.IO) {
        deleteSubjectUseCase(subjectModel.id)
    }
}
