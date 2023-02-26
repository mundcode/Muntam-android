package com.mundcode.designsystem.components.bottomsheets.option

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mundcode.designsystem.components.bottomsheets.BottomSheetContent
import com.mundcode.designsystem.components.texts.OptionText
import com.mundcode.designsystem.theme.MTRed

@Composable
fun SubjectOptionBottomSheetContent(
    onClickClose: () -> Unit,
    onClickDelete: () -> Unit,
    onClickModify: () -> Unit
) {
    BottomSheetContent(title = "과목 리스트 편집", onClickClose = onClickClose) {
        Column {
            OptionText(
                text = "삭제하기",
                textColor = MTRed,
                onClick = onClickDelete
            )
            OptionText(
                text = "수정하기",
                onClick = onClickModify
            )
        }
    }
}



@Preview
@Composable
fun SubjectOptionBottomSheetPreview() {
    SubjectOptionBottomSheetContent({}, {}, {})
}
