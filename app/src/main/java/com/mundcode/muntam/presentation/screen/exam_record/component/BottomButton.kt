package com.mundcode.muntam.presentation.screen.exam_record.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.Gray300
import com.mundcode.designsystem.theme.Gray500
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTTextStyle

@Composable
fun BottomButton(
    resId: Int,
    text: String,
    enable: Boolean,
    onClick: () -> Unit = {}
) {

    Button(onClick = onClick, enabled = enable) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                painter = painterResource(id = resId),
                contentDescription = null,
                tint = if (enable) MTOrange else Gray300
            )
            Text(text = text, style = MTTextStyle.text13, color = Gray500)
        }
    }
}