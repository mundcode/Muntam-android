package com.mundcode.muntam.presentation.ui.exams

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.components.tags.FinishedTag
import com.mundcode.designsystem.components.tags.RunningTag
import com.mundcode.designsystem.theme.Gray600
import com.mundcode.designsystem.theme.Gray800
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.muntam.R
import com.mundcode.muntam.presentation.ui.model.ExamModel

@Composable
fun ExamItem(
    exam: ExamModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (exam.state) {
                ExamState.PAUSE -> {
                    RunningTag(isSmall = true)
                }
                ExamState.END -> {
                    FinishedTag(isSmall = true)
                }
            }

            Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                Icon(
                    painter = painterResource(id = if (exam.isFavorite) R.drawable.ic_save_on_24_dp else R.drawable.ic_save_off_24_dp),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_24_dp),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 6.dp)
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