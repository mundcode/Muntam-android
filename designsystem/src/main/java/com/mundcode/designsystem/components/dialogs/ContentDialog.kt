package com.mundcode.designsystem.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mundcode.designsystem.R
import com.mundcode.designsystem.components.buttons.PrimaryMTButton
import com.mundcode.designsystem.components.buttons.SecondaryMTButton
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.theme.CornerRadius16
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.White

@Composable
fun ContentDialog(
    title: String,
    onClickClose: () -> Unit = {},
    onClickConfirm: () -> Unit = {},
    onClickCancel: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .background(color = White, shape = CornerRadius16)
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row {
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SecondaryMTButton(
                    text = "취소",
                    onClick = onClickCancel,
                    modifier = Modifier.weight(1f)
                )
                PrimaryMTButton(
                    text = "확인",
                    onClick = onClickConfirm,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
