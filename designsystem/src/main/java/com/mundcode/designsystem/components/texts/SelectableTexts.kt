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
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.Transparent
import com.mundcode.designsystem.theme.White

@Composable
fun SelectableNumberText(
    number: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClickNumber: (Int) -> Unit = {},
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .size(40.dp) // todo 텍스트 크게 설정한 경우 고려
            .background(color = if (isSelected) MTOrange else Transparent, shape = CircleShape)
            .clickable {
                onClickNumber(number)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$number",
            style = if (isSelected) MTTextStyle.textBold16 else MTTextStyle.text16,
            color = if (isSelected) White else Gray900,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun SelectableTextPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SelectableNumberText(number = 1, isSelected = true)
        SelectableNumberText(number = 2, isSelected = false)
        SelectableNumberText(number = 3, isSelected = true)
        SelectableNumberText(number = 4, isSelected = false)
        SelectableNumberText(number = 100, isSelected = true)
        SelectableNumberText(number = 100, isSelected = false)
    }
}