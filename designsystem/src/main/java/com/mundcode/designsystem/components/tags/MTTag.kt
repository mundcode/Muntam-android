package com.mundcode.designsystem.components.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.CornerRadius4
import com.mundcode.designsystem.theme.MTGreen
import com.mundcode.designsystem.theme.MTLightGreen
import com.mundcode.designsystem.theme.MTLightOrange
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTTextStyle

@Composable
private fun SmallMTTag(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .background(color = backgroundColor, shape = CornerRadius4)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        style = MTTextStyle.textBold13,
        color = textColor
    )
}

@Composable
private fun BigMTTag(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .background(color = backgroundColor, shape = CornerRadius4)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        style = MTTextStyle.textBold14,
        color = textColor
    )
}

@Preview
@Composable
fun MTTagPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        SmallMTTag(text = "과목명", backgroundColor = MTLightOrange, textColor = MTOrange)
        SmallMTTag(text = "과목명", backgroundColor = MTLightGreen, textColor = MTGreen)
        SmallMTTag(text = "과목명", backgroundColor = MTLightGreen, textColor = MTGreen)
        BigMTTag(text = "과목명", backgroundColor = MTLightGreen, textColor = MTGreen)
        BigMTTag(text = "과목명", backgroundColor = MTLightOrange, textColor = MTOrange)
    }
}
