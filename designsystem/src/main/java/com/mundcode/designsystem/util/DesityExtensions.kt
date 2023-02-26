package com.mundcode.designsystem.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun TextStyle.spToDp(): TextStyle {
    return with(LocalDensity.current) {
        this@spToDp.copy(
            fontSize = fontSize.value.dp.toSp(),
            lineHeight = lineHeight.value.dp.toSp(),
            letterSpacing = letterSpacing.value.dp.toSp()
        )
    }
}
