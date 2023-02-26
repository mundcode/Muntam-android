package com.mundcode.designsystem.components.texts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.model.SelectableTextState

@Composable
fun SelectableNumberText(
    number: Int,
    state: SelectableTextState,
    modifier: Modifier = Modifier,
    onClickNumber: (Int) -> Unit = {},
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .size(40.dp) // todo 텍스트 크게 설정한 경우 고려
            .background(color = state.backgroundColor, shape = CircleShape)
            .clickable {
                if (state != SelectableTextState.UNSELECTABLE) {
                    onClickNumber(number)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$number",
            style = state.textStyle,
            color = state.textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun SelectableTextPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SelectableNumberText(number = 1, state = SelectableTextState.SELECTABLE)
        SelectableNumberText(number = 2, state = SelectableTextState.SELECTED)
        SelectableNumberText(number = 2, state = SelectableTextState.UNSELECTABLE)
        SelectableNumberText(number = 3, state = SelectableTextState.SELECTABLE)
        SelectableNumberText(number = 4, state = SelectableTextState.SELECTED)
        SelectableNumberText(number = 4, state = SelectableTextState.UNSELECTABLE)
        SelectableNumberText(number = 100, state = SelectableTextState.SELECTABLE)
        SelectableNumberText(number = 100, state = SelectableTextState.SELECTED)
        SelectableNumberText(number = 100, state = SelectableTextState.UNSELECTABLE)
    }
}
