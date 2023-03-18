package com.mundcode.designsystem.components.dialogs

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import com.mundcode.designsystem.model.SelectableNumber
import com.mundcode.designsystem.model.SelectableTextState

@Composable
fun JumpNumberPickerDialog(
    selectableNumbers: List<SelectableNumber>,
    currentNumber: Int,
    onSelect: (Int) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    var selectedNumber by remember {
        mutableStateOf(currentNumber)
    }

    ContentDialog(
        title = "총 문항수 설정",
        onClickClose = onCancel,
        onClickCancel = onCancel,
        onClickConfirm = {
            onSelect(selectedNumber)
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(0.dp, 280.dp),
            flingBehavior = ScrollableDefaults.flingBehavior()
        ) {
            items(selectableNumbers) { number ->
                SelectableNumberText(
                    number = number.number,
                    state = if (number.number == selectedNumber) SelectableTextState.SELECTED else number.state,
                    onClickNumber = { selectedNumber = it }
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFF)
@Composable
fun JumpNumberPickerDialogPreview() {
    JumpNumberPickerDialog(
        selectableNumbers =
        (1..200).map {
            SelectableNumber(
                number = it,
                state = if (it % 3 == 0) SelectableTextState.UNSELECTABLE else SelectableTextState.SELECTABLE
            )
        },
        currentNumber = 10
    )
}

@Preview(backgroundColor = 0xFFFFFF)
@Composable
fun JumpNumberPickerDialogPreview2() {
    JumpNumberPickerDialog(
        selectableNumbers =
        (1..20).map {
            SelectableNumber(
                number = it,
                state = if (it % 3 == 0) SelectableTextState.UNSELECTABLE else SelectableTextState.SELECTABLE
            )
        },
        currentNumber = 10
    )
}
