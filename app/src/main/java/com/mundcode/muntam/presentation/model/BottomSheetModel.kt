package com.mundcode.muntam.presentation.model

sealed class BottomSheetModel {
    object None : BottomSheetModel()
    data class SubjectMoreBottomSheet(
        val onClickClose: () -> Unit = {},
        val onClickDelete: () -> Unit,
        val onClickModify: () -> Unit
    ) : BottomSheetModel()
}
