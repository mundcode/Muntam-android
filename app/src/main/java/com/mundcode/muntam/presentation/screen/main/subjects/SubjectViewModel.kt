package com.mundcode.muntam.presentation.screen.main.subjects

import androidx.lifecycle.viewModelScope
import com.mundcode.domain.usecase.DeleteSubjectUseCase
import com.mundcode.domain.usecase.GetSubjectsUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.presentation.model.SubjectModel
import com.mundcode.muntam.presentation.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getSubjectsUseCase: GetSubjectsUseCase,
    private val deleteSubjectUseCase: DeleteSubjectUseCase,
) : BaseViewModel() {
    private val _subjects = MutableStateFlow<List<SubjectModel>>(listOf())
    val subjects: StateFlow<List<SubjectModel>> = _subjects.asStateFlow()

    init {
        loadSubjects()
    }

    private fun loadSubjects() = viewModelScope.launch(Dispatchers.IO) {
        getSubjectsUseCase().collectLatest { list ->
            _subjects.emit(list.map { it.asStateModel() })
        }
    }

    fun deleteSubject(subjectModel: SubjectModel) = viewModelScope.launch(Dispatchers.IO) {
        deleteSubjectUseCase(subjectModel.id)
    }
}
