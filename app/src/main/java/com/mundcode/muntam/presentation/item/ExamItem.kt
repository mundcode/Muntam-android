package com.mundcode.muntam.presentation.screen.exams

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.components.tags.FinishedTag
import com.mundcode.designsystem.components.tags.RunningTag
import com.mundcode.designsystem.theme.Gray200
import com.mundcode.designsystem.theme.Gray400
import com.mundcode.designsystem.theme.Gray600
import com.mundcode.designsystem.theme.Gray800
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.muntam.R
import com.mundcode.muntam.presentation.model.ExamModel
import com.mundcode.muntam.presentation.model.createExamModel
import com.mundcode.muntam.presentation.model.createExamModels

@Composable
fun ExamItem(
    exam: ExamModel,
    onClickSave: () -> Unit = {},
    onClickMore: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (exam.state) {
                ExamState.END -> {
                    FinishedTag(isSmall = true)
                }
                else -> {
                    RunningTag(isSmall = true)
                }
            }

            Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                Icon(
                    painter = painterResource(
                        id = if (exam.isFavorite) {
                            R.drawable.ic_save_on_24_dp
                        } else {
                            R.drawable.ic_save_off_24_dp
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            onClick = onClickSave,
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        )
                        .padding(end = 6.dp),
                    tint = if (exam.isFavorite) MTOrange else Gray400
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_more_24_dp),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            onClick = onClickMore,
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        )
                        .padding(start = 6.dp),
                    tint = Gray400
                )
            }
        }

        Text(
            text = exam.name,
            style = MTTextStyle.textBold16,
            color = Gray800,
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = exam.createdAtText, style = MTTextStyle.text13, color = Gray600)

        Text(
            text = "초과시간 · ${exam.expiredTimeText ?: "없움"}",
            style = MTTextStyle.text13,
            color = exam.expiredTimeTextColor
        )
    }
}

@Preview
@Composable
fun ExamItemPreview() {
    val exam = createExamModel(
        id = 1,
        subjectId = 1
    )

    ExamItem(exam = exam)
}

@Preview
@Composable
fun ExamItemListPreview() {
    val exams = createExamModels(100, 1)
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(exams) { index, exam ->
            Column {
                ExamItem(exam = exam)
                if (index != exams.lastIndex) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        color = Gray200
                    )
                }
            }
        }
    }
}
