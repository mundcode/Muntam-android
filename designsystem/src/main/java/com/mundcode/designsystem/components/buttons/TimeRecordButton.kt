package com.mundcode.designsystem.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mundcode.designsystem.R
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.theme.CornerRadius12
import com.mundcode.designsystem.theme.MTGreen
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.Transparent
import com.mundcode.designsystem.theme.White

@Composable
fun TimeRecordButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MTGreen, shape = CornerRadius12)
            .clip(CornerRadius12)
            .clickable(onClick = onClick)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.background(Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⏰",
                fontSize = with(LocalDensity.current) { 24.sp.toDp().value.sp }
            )
            Margin(dp = 12.dp)
            Text(
                text = stringResource(id = R.string.title_record_time),
                style = MTTextStyle.textBold16,
                color = White,
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_16_dp),
            tint = White,
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun TimeRecordButtonPreview() {
    TimeRecordButton()
}
