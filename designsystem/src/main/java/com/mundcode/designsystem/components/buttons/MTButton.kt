package com.mundcode.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.CornerRadius12
import com.mundcode.designsystem.theme.Gray500
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.White

@Composable
fun MTButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    strokeColor: Color? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.clip(CornerRadius12),
        enabled = enabled,
        elevation = null,
        shape = CornerRadius12,
        border = if (strokeColor == null) null else BorderStroke(width = 1.dp, color = strokeColor),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            disabledBackgroundColor = Gray500
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            text = text,
            style = MTTextStyle.textBold16,
            color = textColor
        )
    }
}


@Preview(name = "MTButtonPreview")
@Composable
fun MTButtonPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        MTButton(
            text = "🕰 과목 추가하기",
            backgroundColor = MTOrange,
            textColor = White,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // do nothing
            }
        )

        MTButton(
            text = "과목 추가하기",
            backgroundColor = Color.Transparent,
            strokeColor = MTOrange,
            textColor = MTOrange,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // do nothing
            }
        )
    }
}