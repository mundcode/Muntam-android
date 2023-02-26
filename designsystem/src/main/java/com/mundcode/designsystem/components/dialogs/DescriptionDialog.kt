package com.mundcode.designsystem.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.mundcode.designsystem.components.buttons.PrimaryMTButton
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.theme.CornerRadius16
import com.mundcode.designsystem.theme.Gray700
import com.mundcode.designsystem.theme.MTTextStyle

@Composable
fun DescriptionDialog(
    title: String,
    description: String,
    buttonsText: String = "확인",
    onClickConfirm: () -> Unit
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
                text = description,
                style = MTTextStyle.text14,
                color = Gray700,
                textAlign = TextAlign.Center
            )

            Margin(dp = 20.dp)

            PrimaryMTButton(
                text = buttonsText,
                onClick = onClickConfirm
            )
        }
    }
}

@Preview
@Composable
fun DescriptionDialogPreview() {
    DescriptionDialog(title = "글자수 초과", description = "허용된 글자수를 초과하였습니다.\n2000자 이내로 작성해주세요.") {}
}
