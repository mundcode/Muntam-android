package com.mundcode.muntam.presentation.screen.exam_record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.components.toolbars.MTTitleToolbar
import com.mundcode.designsystem.theme.Gray100
import com.mundcode.designsystem.theme.Gray300
import com.mundcode.designsystem.theme.Gray500
import com.mundcode.designsystem.theme.Gray700
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTLightOrange
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTRed
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.muntam.R
import com.mundcode.muntam.presentation.screen.exam_record.component.BottomButton
import com.mundcode.muntam.presentation.screen.exam_record.component.TimerCircularProgressBar
import com.mundcode.muntam.presentation.screen.exam_record.component.TopExamState
import com.mundcode.muntam.util.hiltViewModel

@Composable
fun ExamRecordScreen(
    viewModel: ExamRecordViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    // todo 리스너 달기

    Scaffold(
        topBar = {
            MTTitleToolbar(
                onClickBack = viewModel::onClickBack,
                title = state.examModel.name,
                icons = listOf(
                    {
                        Icon(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 20.dp, end = 12.dp)
                                .clickable(
                                    onClick = viewModel::onClickSetting,
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ),
                            painter = painterResource(id = R.drawable.ic_setttings_24_dp),
                            contentDescription = null,
                            tint = Gray900
                        )
                    },
                    {
                        Icon(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(end = 20.dp)
                                .clickable(
                                    onClick = viewModel::onClickSetting,
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ),
                            painter = painterResource(id = R.drawable.ic_check_24_dp),
                            contentDescription = null,
                            tint = Gray900
                        )
                    }
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomButton(
                    resId = R.drawable.ic_pause_off_56_dp,
                    text = "일시 정지",
                    enable = state.examModel.state == ExamState.RUNNING,
                    onClick = viewModel::onClickPause
                )

                BottomButton(
                    resId = R.drawable.ic_skip_56_dp,
                    text = "문제 건너뛰기",
                    enable = state.examModel.state == ExamState.RUNNING,
                    onClick = viewModel::onClickJump
                )
            }
        }
    ) { paddingValues ->

        when (state.examModel.state) {
            ExamState.READY -> {
                ExamRecordTimerScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    backgroundLineColor = MTLightOrange,
                    lineColor = MTOrange,
                    newPercentage = 0f,
                    currentTime = "00:00:00",
                    currentTimeColor = Gray500,
                    remainTime = "00:00:00",
                    remainTimeColor = Gray300,
                    topStateComposable = {
                        TopExamState(
                            emoji = "💬",
                            stateContent = {
                                Text(
                                    text = "시작 대기 중",
                                    style = MTTextStyle.text20,
                                    color = Gray700
                                )
                            }
                        )
                    },
                    bottomStateComposable = {
                        Text(
                            text = "화면을 터치하면 기록이 시작됩니다.",
                            style = MTTextStyle.textBold16,
                            color = MTOrange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    },
                    onClickScreen = viewModel::onClickScreen
                )
            }
            ExamState.RUNNING -> {
                ExamRecordTimerScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    backgroundLineColor = MTLightOrange,
                    lineColor = MTOrange,
                    newPercentage =
                    (state.examModel.lastAt
                        ?.div(state.timeLimit.toFloat()))
                        ?.times(100) ?: 0f,
                    currentTime = state.currentExamTimeText,
                    currentTimeColor = Gray900,
                    remainTime = state.remainExamTimeText,
                    remainTimeColor = MTRed,
                    topStateComposable = {
                        TopExamState(
                            emoji = "✍️",
                            stateContent = {
                                val annotatedText = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = MTTextStyle.textBold20.fontWeight
                                        )
                                    ) {
                                        append("${state.examModel.lastQuestionNumber}번")
                                    }
                                    append(" 문제 푸는 중")
                                }
                                Text(
                                    text = annotatedText,
                                    style = MTTextStyle.text20,
                                    color = Gray900
                                )
                            }
                        )
                    },
                    bottomStateComposable = {},
                    onClickScreen = viewModel::onClickScreen
                )
            }
            ExamState.PAUSE -> {
                ExamRecordTimerScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    backgroundLineColor = Gray100,
                    lineColor = Gray300,
                    newPercentage = (state.examModel.lastAt
                        ?.div(state.timeLimit.toFloat()))
                        ?.times(100) ?: 0f,
                    currentTime = state.currentExamTimeText,
                    currentTimeColor = Gray900,
                    remainTime = state.remainExamTimeText,
                    remainTimeColor = MTRed,
                    topStateComposable = {
                        TopExamState(
                            emoji = "💬",
                            stateContent = {
                                Text(text = "일시 정지 중", style = MTTextStyle.text20, color = Gray700)
                            }
                        )
                    },
                    bottomStateComposable = {
                        Text(
                            text = "화면을 터치하면 다시 시작됩니다.",
                            style = MTTextStyle.textBold16,
                            color = Gray700,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            maxLines = 1
                        )
                    },
                    onClickScreen = viewModel::onClickScreen
                )
            }
            ExamState.END -> {
                ExamRecordTimerScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    backgroundLineColor = MTOrange,
                    lineColor = MTOrange,
                    newPercentage =
                    (state.examModel.lastAt
                        ?.div(state.timeLimit.toFloat()))
                        ?.times(100) ?: 0f,
                    currentTime = state.currentExamTimeText,
                    currentTimeColor = Gray900,
                    remainTime = state.remainExamTimeText,
                    remainTimeColor = Gray300,
                    topStateComposable = {
                        TopExamState(
                            emoji = "👏",
                            stateContent = {
                                Text(
                                    text = "문제풀이 완료",
                                    style = MTTextStyle.text20,
                                    color = MTOrange
                                )
                            }
                        )
                    },
                    bottomStateComposable = {
                        Text(
                            text = "완벽하게 끝났어요!",
                            style = MTTextStyle.textBold16,
                            color = Gray700,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            maxLines = 1
                        )
                    },
                    onClickScreen = viewModel::onClickScreen
                )
            }
        }
    }
}

@Composable
fun ExamRecordTimerScreen(
    modifier: Modifier = Modifier,
    backgroundLineColor: Color,
    lineColor: Color,
    prevPercentage: Float = 0f,
    newPercentage: Float,
    currentTime: String,
    currentTimeColor: Color = Gray900,
    remainTime: String,
    remainTimeColor: Color = MTRed,
    onClickScreen: () -> Unit,
    topStateComposable: @Composable () -> Unit = {},
    bottomStateComposable: @Composable () -> Unit = {}
) {
    // todo 스크롤러블로 수정
    Column(
        modifier = modifier.clickable(onClick = onClickScreen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        topStateComposable()

        Margin(dp = 24.dp)

        TimerCircularProgressBar(
            modifier = Modifier.size(296.dp),
            backgroundLineColor = backgroundLineColor,
            lineColor = lineColor,
            prevPercentage = prevPercentage,
            newPercentage = newPercentage,
            currentTime = currentTime,
            currentTimeColor = currentTimeColor,
            remainTime = remainTime,
            remainTimeColor = remainTimeColor
        )

        Margin(dp = 24.dp)

        bottomStateComposable()
    }
}

