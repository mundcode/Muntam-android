package com.mundcode.designsystem.components.texts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.Gray700
import com.mundcode.designsystem.theme.MTTextStyle


@Composable
fun OptionText(
    text: String,
    textColor: Color = Gray700,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = MTTextStyle.text16,
        color = textColor,
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    )
}