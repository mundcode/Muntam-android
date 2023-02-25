package com.mundcode.designsystem.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.components.texts.SelectableNumberText
import com.mundcode.designsystem.model.SelectableTextState

@Composable
fun NumberJumpDialog(
    currentNumber: Int,

    onResult: (Int) -> Unit,
    onCancel: () -> Unit
) {
    var selectedNumber by remember {
        mutableStateOf(1)
    }

    SelectableDialog(
        title = "총 문항수 설정",
        onClickClose = onCancel,
        onClickCancel = onCancel,
        onClickConfirm = {
            onResult(selectedNumber)
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .height(280.dp)
                .fillMaxWidth()
        ) {
            items((1..200).toList()) { number ->
                SelectableNumberText(
                    number = number,
                    state = if (number == selectedNumber) SelectableTextState.SELECTED else SelectableTextState.SELECTABLE,
                    onClickNumber = { selectedNumber = it })
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFF)
@Composable
fun NumberJumpDialogPreview() {
    TotalNumberPickerDialog(
        onResult = {},
        onCancel = {}
    )
}