package com.mundcode.designsystem.components.dialogs.alert

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mundcode.designsystem.components.buttons.AlertMTButton
import com.mundcode.designsystem.components.buttons.SecondaryMTButton
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.theme.CornerRadius16
import com.mundcode.designsystem.theme.Gray700
import com.mundcode.designsystem.theme.MTTextStyle

@Composable
fun AlertDialog(
    title: String,
    subtitle: String,
    cancelText: String = "취소",
    confirmText: String,
    onClickCancel: () -> Unit = {},
    onClickConfirm: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.background,
                    shape = CornerRadius16
                )
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MTTextStyle.textBold20,
                color = MaterialTheme.colors.onBackground
            )

            Margin(dp = 8.dp)

            Text(
                text = subtitle,
                style = MTTextStyle.text14,
                color = Gray700,
                textAlign = TextAlign.Center
            )

            Margin(dp = 20.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SecondaryMTButton(
                    text = cancelText,
                    onClick = onClickCancel,
                    modifier = Modifier.weight(1f)
                )
                AlertMTButton(
                    text = confirmText,
                    onClick = onClickConfirm,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview
@Composable
fun AlertDialogPreview() {
    AlertDialog(
        title = "과목 삭제하기",
        subtitle = "선택한 과목 리스트를 삭제합니다.\n삭제한 항목은 다시 되돌릴 수 없습니다.",
        cancelText = "취소",
        confirmText = "삭제하기",
        onClickCancel = {},
        onClickConfirm = {}
    )
}
