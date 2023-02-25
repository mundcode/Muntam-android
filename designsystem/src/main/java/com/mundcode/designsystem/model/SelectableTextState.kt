package com.mundcode.designsystem.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.mundcode.designsystem.theme.Gray400
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.Transparent
import com.mundcode.designsystem.theme.White

enum class SelectableTextState(
    val backgroundColor: Color,
    val textStyle: TextStyle,
    val textColor: Color
) {
    SELECTED(backgroundColor = MTOrange, textStyle = MTTextStyle.textBold16, textColor = White),
    SELECTABLE(backgroundColor = Transparent, textStyle = MTTextStyle.text16, textColor = Gray900),
    UNSELECTABLE(backgroundColor = Transparent, textStyle = MTTextStyle.text16, textColor = Gray400)
}