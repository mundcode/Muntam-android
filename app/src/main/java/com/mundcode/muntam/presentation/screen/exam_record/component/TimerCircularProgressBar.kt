package com.mundcode.muntam.presentation.screen.exam_record.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.theme.Gray100
import com.mundcode.designsystem.theme.Gray500
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTRed
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.util.spToDp

const val PROGRESS_ANIM_DURATION_MILLIES = 1000
const val LINE_WIDTH = 24

@Composable
fun TimerCircularProgressBar(
    modifier: Modifier = Modifier,
    backgroundLineColor: Color,
    lineColor: Color,
    prevPercentage: Float = 0f,
    newPercentage: Float,
    currentTime: String,
    currentTimeColor: Color = Gray900,
    remainTime: String,
    remainTimeColor: Color = MTRed
) {
    val currentPercentage = remember { Animatable(prevPercentage) }

    LaunchedEffect(newPercentage) {
        currentPercentage.animateTo(
            newPercentage, animationSpec = tween(durationMillis = PROGRESS_ANIM_DURATION_MILLIES)
        )
    }


    Box(
        modifier = modifier.padding((LINE_WIDTH / 2).dp), contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = backgroundLineColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = LINE_WIDTH.dp.toPx())
            )

            drawArc(
                color = lineColor,
                startAngle = -90f,
                sweepAngle = 360f * newPercentage,
                useCenter = false,
                style = Stroke(width = LINE_WIDTH.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimeText(description = "현재시간", timeText = currentTime, color = currentTimeColor)
            TimeText(description = "남은시간", timeText = remainTime, color = remainTimeColor)
        }
    }
}

@Composable
fun TimeText(
    modifier: Modifier = Modifier,
    description: String,
    timeText: String,
    color: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = description, style = MTTextStyle.text13.spToDp(), color = Gray500)
        Margin(dp = 8.dp)
        Text(text = timeText, style = MTTextStyle.textBold32.spToDp(), color = color)
    }
}

@Preview(widthDp = 300, heightDp = 300)
@Composable
fun TimerCircularProgressBarPreview() {
    TimerCircularProgressBar(
        backgroundLineColor = Gray100,
        lineColor = MTOrange,
        newPercentage = 0.8f,
        modifier = Modifier.size(296.dp),
        currentTime = "02:23:11",
        remainTime = "00:37:49"
    )
}

@Preview(widthDp = 300, heightDp = 300)
@Composable
fun TimerCircularProgressBarPreview2() {
    TimerCircularProgressBar(
        backgroundLineColor = Gray100,
        lineColor = MTOrange,
        prevPercentage = 0.5f,
        newPercentage = 0.8f,
        modifier = Modifier.size(296.dp),
        currentTime = "02:23:11",
        remainTime = "00:37:49"
    )
}
