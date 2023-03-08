package com.mundcode.muntam.presentation.screen.exam_record

import androidx.lifecycle.SavedStateHandle
import com.mundcode.muntam.navigation.ExamRecord
import com.mundcode.muntam.navigation.SubjectModify
import com.mundcode.muntam.presentation.model.ExamModel
import com.mundcode.muntam.presentation.model.SubjectModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExamRecordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) {
    private val subjectId: Int = checkNotNull(savedStateHandle[ExamRecord.subjectIdArg])
    private val examId: Int = checkNotNull(savedStateHandle[ExamRecord.examIdArg])


}

data class ExamRecordState(
    val subjectModel: SubjectModel,
    val examModel: ExamModel,
    val showBackConfirmDialog: Boolean,
    val showCompleteDialog: Boolean,
    val showJumpQuestionDialog: Boolean
)