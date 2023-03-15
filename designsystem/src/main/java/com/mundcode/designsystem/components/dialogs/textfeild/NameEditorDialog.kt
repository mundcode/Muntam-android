package com.mundcode.designsystem.components.dialogs.textfeild

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mundcode.designsystem.components.dialogs.ContentDialog
import com.mundcode.designsystem.components.texts.NameTextField

@Composable
fun NameEditorDialog(
    title: String = "이름 수정하기",
    hint: String = "시험명을 입력해주세요",
    onClickCancel: () -> Unit,
    onClickConfirm: (String) -> Unit
) {
    var value by remember {
        mutableStateOf("")
    }

    ContentDialog(
        title = title,
        onClickClose = onClickCancel,
        onClickCancel = onClickCancel,
        onClickConfirm = {
            onClickConfirm(value)
        }
    ) {
        NameTextField(
            value = value,
            onValueChange = {
                value = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholderText = hint
        )
    }
}

@Preview(widthDp = 500)
@Composable
fun NameEditorDialogPreview() {
    NameEditorDialog(onClickCancel = {}, onClickConfirm = {})
}
