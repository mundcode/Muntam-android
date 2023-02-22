package com.mundcode.muntam.presentation.ui.subject_add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.DefaultSpace16
import com.mundcode.designsystem.theme.DefaultSpace8
import com.mundcode.muntam.presentation.ui.component.MarginSpacer
import com.mundcode.muntam.presentation.ui.component.MuntamToolbar

@Composable
fun SubjectAddScreen() {
    Scaffold(
        topBar = {
            MuntamToolbar(
                showBack = true,
                title = "과목 추가",
                icons =
                listOf {
                    Text(
                        text = "저장",
                        style = MaterialTheme.typography.caption,
                        color = Color.Black
                    )
                }
            )
        }
    ) { paddingValues ->
        SubjectAddContent(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun SubjectAddContent(
    modifier: Modifier = Modifier
) {
}

@Composable
fun MuntamTextField(
    modifier: Modifier = Modifier,
    title: String,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = DefaultSpace16, top = DefaultSpace8)
        )

        MarginSpacer(dp = 8.dp)

        TextField(
            value = value,
            onValueChange = onValueChange,
        )
    }
}

@Preview
@Composable
fun PreviewMuntamTextField() {
    var value by remember {
        mutableStateOf("")
    }
    MuntamTextField(
        title = "과목명",
        hint = "과목명을 입력해주세요",
        value = value,
        onValueChange = { value = it }
    )
}
