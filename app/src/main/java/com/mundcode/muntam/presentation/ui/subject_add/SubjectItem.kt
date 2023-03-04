package com.mundcode.muntam.presentation.ui.subject_add

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
import com.mundcode.muntam.presentation.ui.model.SubjectModel
import com.mundcode.muntam.presentation.ui.model.createMockedSubjectModel

@Composable
fun SubjectItem(
    subject: SubjectModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .size(width = 162.dp, height = 156.dp)
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
                text = subject.imoji,
                style = MTTextStyle.text20.spToDp(),
                modifier = Modifier.padding(2.dp),
                maxLines = 1
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_more_24_dp),
                contentDescription = null
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            SubjectNameTag(subject.subjectTitle)
            if (subject.lastExamName != null && subject.lastExamDateText != null) {
                Text(
                    text = subject.lastExamName,
                    style = MTTextStyle.text16.spToDp(),
                    color = Gray800,
                    maxLines = 2
                )
                Text(
                    text = subject.lastExamDateText,
                    style = MTTextStyle.text13.spToDp(),
                    color = Gray600,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview
@Composable
fun SubjectItemPreview() {
    val model = createMockedSubjectModel(1)
    SubjectItem(subject = model, onClick = {})
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
            SubjectItem(subject = item, onClick = {}, modifier = Modifier.padding(bottom = 12.dp))
        }
    }
}
