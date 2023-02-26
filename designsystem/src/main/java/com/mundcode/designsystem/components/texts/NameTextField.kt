package com.mundcode.designsystem.components.texts

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.CornerRadius12
import com.mundcode.designsystem.theme.Gray300
import com.mundcode.designsystem.theme.Gray400
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.Transparent

@Composable
fun NameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    readOnly: Boolean = false,

    ) {
    MTBasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.border(width = 1.dp, shape = CornerRadius12, color = Gray300),
        readOnly = readOnly,
        textStyle = MTTextStyle.text16,
        placeholder = {
            Text(text = placeholderText, style = MTTextStyle.text16, color = Gray400)
        },
        singleLine = true,
        shape = CornerRadius12,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Gray900,
            disabledTextColor = Gray900,
            backgroundColor = Transparent,
            cursorColor = Gray900,
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent
        )
    )
}

@Preview
@Composable
fun NameTextFieldPreview() {
    var value by remember {
        mutableStateOf("")
    }
    NameTextField(
        value = value,
        onValueChange = {
            value = it
        },
        modifier = Modifier.fillMaxWidth(),
        placeholderText = "시험명을 입력해주세요"
    )
}
