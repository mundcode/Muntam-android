package com.mundcode.muntam.presentation.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.data.local.database.model.asExternalModel
import com.mundcode.data.local.database.model.createQuestionEntity
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.theme.Gray500
import com.mundcode.designsystem.theme.Gray700
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTLightOrange
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTRed
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.util.spToDp
import com.mundcode.muntam.presentation.model.QuestionModel
import com.mundcode.muntam.presentation.model.asStateModel

@Composable
fun QuestionItem(
    modifier: Modifier = Modifier,
    questionModel: QuestionModel,
    onClickCorrect: () -> Unit = {},
    onClickAlarm: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Top,
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${questionModel.questionNumber}번",
                style = MTTextStyle.textBold20.spToDp(),
                color = Gray900,
                textAlign = TextAlign.Start
            )

            Margin(dp = 4.dp)

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = if (questionModel.isCorrect) Gray500 else MTRed,
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .height(IntrinsicSize.Max)
                    .clickable(
                        onClick = onClickCorrect,
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    )
            )
        }

        Margin(dp = 16.dp)

        Column(
            modifier = Modifier
                .weight(1f)
                .height(IntrinsicSize.Max),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${questionModel.lapsedTimeText} ",
                style = MTTextStyle.textBold16.spToDp(),
                color = Gray900
            )

            Margin(dp = 8.dp)

            Text(
                text = "이 때의 시험 시간 : ${questionModel.lapsedExamTimeText}",
                style = MTTextStyle.textBold14.spToDp(),
                color = Gray700
            )
        }

        Column(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .align(Alignment.CenterVertically)
                .clickable(
                    onClick = onClickAlarm,
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (questionModel.isAlarm) MTOrange else MTLightOrange,
            )
            Text(
                text = if (questionModel.isAlarm) "알림 ON" else "알람 OFF",
                style = MTTextStyle.text10,
                color = if (questionModel.isAlarm) MTOrange else MTLightOrange
            )
        }
    }
}

@Preview
@Composable
fun QuestionItemPreview() {
    Column {

        QuestionItem(
            questionModel = createQuestionEntity(1, 1, 1).asExternalModel().asStateModel()
                .copy(isCorrect = false, isAlarm = false)
        )
        QuestionItem(
            questionModel = createQuestionEntity(1, 1, 1).asExternalModel().asStateModel()
                .copy(isCorrect = true, isAlarm = true)
        )
    }
}