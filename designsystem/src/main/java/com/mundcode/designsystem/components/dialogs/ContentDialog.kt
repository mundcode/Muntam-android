package com.mundcode.designsystem.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mundcode.designsystem.R
import com.mundcode.designsystem.components.buttons.PrimaryMTButton
import com.mundcode.designsystem.components.buttons.SecondaryMTButton
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.components.texts.SelectableNumberText
import com.mundcode.designsystem.theme.CornerRadius16
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.White

@Composable
fun SelectableDialog(
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
                .padding(top = 24.dp, bottom = 20.dp)
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

            Margin(dp = 20.dp)

            content()

            Margin(dp = 20.dp)

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

@Composable
fun TotalNumberPickerDialog(
    onResult: (Int) -> Unit,
    onCancel: () -> Unit
) {
    var selectedNumber by remember {
        mutableStateOf(1)
    }

    SelectableDialog(
        title = "총 문항수 설정",
        onClickClose = onCancel,
        onClickCancel = onCancel,
        onClickConfirm = {
            onResult(selectedNumber)
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .height(280.dp)
                .fillMaxWidth()
        ) {
            items((1..200).toList()) { number ->
                SelectableNumberText(
                    number = number,
                    isSelected = number == selectedNumber,
                    onClickNumber = { selectedNumber = it })
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFF)
@Composable
fun TotalNumberPickerDialogPreview() {
    TotalNumberPickerDialog(
        onResult = {},
        onCancel = {}
    )
}