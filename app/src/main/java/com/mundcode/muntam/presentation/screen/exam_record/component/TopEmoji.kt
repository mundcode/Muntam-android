package com.mundcode.muntam.presentation.screen.exam_record.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.mundcode.designsystem.theme.MTTextStyle

@Composable
fun TopEmoji(
    emoji: String
) {
    Text(text = emoji, style = MTTextStyle.text20)
}
