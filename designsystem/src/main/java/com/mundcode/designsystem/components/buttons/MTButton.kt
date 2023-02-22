package com.mundcode.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.mundcode.designsystem.theme.Gray200
import com.mundcode.designsystem.theme.Gray500
import com.mundcode.designsystem.theme.Gray700
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.White

@Composable
fun MTButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    isBold: Boolean = true,
    modifier: Modifier = Modifier,
    strokeColor: Color? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
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
            disabledBackgroundColor = Gray200
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            text = text,
            style = if (isBold) MTTextStyle.textBold16 else MTTextStyle.text16,
            color = textColor
        )
    }
}

@Composable
fun PrimaryMTButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MTButton(
        text = text,
        backgroundColor = MTOrange,
        textColor = White,
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    )
}

@Composable
fun DisableMTButton(
    text: String,
    modifier: Modifier = Modifier
) {
    MTButton(
        text = text,
        backgroundColor = Gray200,
        textColor = Gray500,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun SecondaryMTButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MTButton(
        text = text,
        backgroundColor = Gray200,
        textColor = Gray700,
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        isBold = false
    )
}

@Composable
fun OutlinedPrimaryMTButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MTButton(
        text = text,
        backgroundColor = Color.Transparent,
        strokeColor = MTOrange,
        textColor = MTOrange,
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    )
}

@Preview(name = "MTButtonPreview")
@Composable
fun MTButtonPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PrimaryMTButton(text = "과목 추가하기", onClick = {})
        
        DisableMTButton(text = "과목 추가하기")

        OutlinedPrimaryMTButton(text = "과목 추가하기", onClick = {})

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SecondaryMTButton(text = "취소", onClick = {}, modifier = Modifier.weight(1f))
            PrimaryMTButton(text = "확인", onClick = {}, modifier = Modifier.weight(1f))
        }
    }

}