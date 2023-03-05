package com.mundcode.muntam.presentation.screen.subject_add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.components.tags.SubjectNameTag
import com.mundcode.designsystem.theme.CornerRadius12
import com.mundcode.designsystem.theme.Gray600
import com.mundcode.designsystem.theme.Gray800
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.White
import com.mundcode.designsystem.util.spToDp
import com.mundcode.muntam.R
import com.mundcode.muntam.presentation.model.SubjectModel
import com.mundcode.muntam.presentation.model.createMockedSubjectModel

const val SUBJECT_ITEM_WIDTH_DP = 162
const val SUBJECT_ITEM_HEIGHT_DP = 156

@Composable
fun SubjectItem(
    subject: SubjectModel,
    onClick: () -> Unit,
    onClickMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .size(width = SUBJECT_ITEM_WIDTH_DP.dp, height = SUBJECT_ITEM_HEIGHT_DP.dp)
            .clip(shape = CornerRadius12)
            .clickable(onClick = onClick)
            .background(color = White, shape = CornerRadius12)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // todo 컴포즈로 이모지 API 제대로 사용하기
            Text(
                text = subject.emoji,
                style = MTTextStyle.text20.spToDp(),
                modifier = Modifier.padding(2.dp),
                maxLines = 1
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_more_24_dp),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onClickMore)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            SubjectNameTag(subject.subjectTitle)

            Text(
                    text = subject.lastExamName ?: "최근 본 시험 없음",
                    style = MTTextStyle.text16.spToDp(),
                    color = Gray800,
                    maxLines = 2
                )
                Text(
                    text = subject.lastExamDateText ?: "클릭해서 시험보기",
                    style = MTTextStyle.text13.spToDp(),
                    color = Gray600,
                    maxLines = 1
                )

        }
    }
}

@Preview
@Composable
fun SubjectItemPreview() {
    val model = createMockedSubjectModel(1)
    SubjectItem(subject = model, onClick = {}, onClickMore = {})
}

@Preview
@Composable
fun SubjectItemInGridPreView() {
    val list = (1..100).map {
        createMockedSubjectModel(it)
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(162.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(20.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(list) { item ->
            SubjectItem(
                subject = item,
                onClick = {},
                modifier = Modifier.padding(bottom = 12.dp), onClickMore = {}
            )
        }
    }
}
