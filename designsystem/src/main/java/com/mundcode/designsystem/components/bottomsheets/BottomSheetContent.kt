package com.mundcode.designsystem.components.bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.R
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.TopCornerRadius12
import com.mundcode.designsystem.theme.White

@Composable
fun BottomSheetContent(
    title: String,
    onClickClose: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = White, shape = TopCornerRadius12)
            .padding(top = 24.dp, bottom = 34.dp),
    ) {
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = title,
                style = MTTextStyle.textBold20,
                modifier = Modifier.weight(1f)
            )
            Margin(dp = 16.dp)
            Icon(
                painter = painterResource(id = R.drawable.ic_close_24_dp),
                contentDescription = null,
                modifier = Modifier.clickable(onClick = onClickClose)
            )
        }
        content()
    }
}
