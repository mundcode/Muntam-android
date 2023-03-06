package com.mundcode.muntam.presentation.screen.subject_add

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.components.buttons.DisableMTButton
import com.mundcode.designsystem.components.buttons.PrimaryMTButton
import com.mundcode.designsystem.components.dialogs.TotalNumberPickerDialog
import com.mundcode.designsystem.components.dialogs.picker.TimeLimitPickerDialog
import com.mundcode.designsystem.components.dialogs.textfeild.NameEditorDialog
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.components.tags.SubjectNameTag
import com.mundcode.designsystem.components.toolbars.MTTitleToolbar
import com.mundcode.designsystem.theme.CornerRadius12
import com.mundcode.designsystem.theme.Gray300
import com.mundcode.designsystem.theme.Gray500
import com.mundcode.designsystem.theme.Gray600
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.muntam.R
import com.mundcode.muntam.util.hiltViewModel

@Composable
fun SubjectAddScreen(
    viewModel: SubjectAddViewModel = hiltViewModel(),
    onClickBack: () -> Unit = {},
) {
    val state by viewModel.subjectAddState.collectAsState()

    Scaffold(
        topBar = {
            MTTitleToolbar(
                title = "과목 추가",
                showBack = true,
                onClickBack = onClickBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                SubjectNameSection(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.subjectName,
                    emoji = state.emoji,
                    onClickName = viewModel::onClickSubjectName,
                    onClickEmoji = viewModel::onClickEmoji
                )

                Margin(dp = 32.dp)

                ReadOnlyOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.timeLimitText,
                    placeholder = "제한 시간",
                    emoji = "⏰",
                    onClick = viewModel::onClickTimeLimit
                )

                Margin(dp = 12.dp)

                ReadOnlyOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = if (state.totalQuestionNumber != 0) {
                        "${state.totalQuestionNumber}개"
                    } else {
                        ""
                    },
                    placeholder = "총 문항수",
                    emoji = "✍️",
                    onClick = viewModel::onClickTotalQuestionNumber
                )

                SubjectAddDescriptionText(desc = "완료 후 변경이 불가합니다.")
            }

            if (state.enableButton) {
                PrimaryMTButton(text = "완료", onClick = viewModel::onClickCompleteButton)
            } else {
                DisableMTButton(text = "완료")
            }
        }

        if (state.showNameDialog) {
            NameEditorDialog(
                hint = "과목명을 입력해주세요",
                onClickCancel = viewModel::onCancelDialog,
                onClickConfirm = viewModel::onSelectSubjectName
            )
        }

        if (state.showTimeLimitDialog) {
            TimeLimitPickerDialog(
                onResult = viewModel::onSelectTimeLimit,
                onCancel = viewModel::onCancelDialog
            )
        }

        if (state.showTotalQuestionNumberDialog) {
            TotalNumberPickerDialog(
                onResult = viewModel::onSelectTotalQuestionNumber,
                onCancel = viewModel::onCancelDialog
            )
        }

        if (state.completeSubjectAddition) {
            onClickBack()
        }
    }
}

@Composable
fun SubjectNameSection(
    modifier: Modifier = Modifier,
    value: String = "",
    emoji: String,
    onClickName: () -> Unit,
    onClickEmoji: () -> Unit
) {
    Column(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = emoji,
                style = MTTextStyle.text16,
                modifier = Modifier
                    .clip(CornerRadius12)
                    .clickable {
                        onClickEmoji()
                    }
                    .border(width = 1.dp, color = Gray300, shape = CornerRadius12)
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CornerRadius12)
                    .clickable(onClick = onClickName)
                    .border(width = 1.dp, color = Gray300, shape = CornerRadius12)
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = "과목 카테고리",
                        style = MTTextStyle.textBold16,
                        color = Gray600,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    SubjectNameTag(name = value)
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right_16_dp),
                    contentDescription = null,
                    tint = Gray500,
                )
            }
        }

        SubjectAddDescriptionText(desc = "과목 카테고리 생성 후 다음 안내 사항")
    }
}

@Composable
fun ReadOnlyOutlinedTextField(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    emoji: String = "",
    value: String = "",
    placeholder: String = "",
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(CornerRadius12)
            .clickable(onClick = onClick)
            .border(width = 1.dp, color = Gray300, shape = CornerRadius12)
            .padding(vertical = 16.dp, horizontal = 20.dp)
    ) {
        Text(text = emoji, style = MTTextStyle.text16, modifier = Modifier.padding(2.dp))

        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = MTTextStyle.textBold16,
                color = Gray600,
                modifier = Modifier.weight(1f)
            )
        } else {
            Text(
                text = value,
                style = MTTextStyle.text16,
                color = Gray900,
                modifier = Modifier.weight(1f)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_16_dp),
            contentDescription = null,
            tint = Gray500
        )
    }
}

@Composable
fun SubjectAddDescriptionText(
    modifier: Modifier = Modifier,
    desc: String
) {
    Text(
        text = desc,
        style = MTTextStyle.text13,
        color = Gray500,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    )
}

@Preview
@Composable
fun SubjectNameSectionPreview() {
    Column() {
        SubjectNameSection(
            value = "",
            emoji = "🥰",
            onClickEmoji = {},
            onClickName = {}
        )
        SubjectNameSection(
            value = "수능 영어",
            emoji = "\uD83E\uDD21",
            onClickEmoji = {},
            onClickName = {}
        )
    }
}

@Preview
@Composable
fun SubjectAddScreenPreview() {
    SubjectAddScreen()
}
