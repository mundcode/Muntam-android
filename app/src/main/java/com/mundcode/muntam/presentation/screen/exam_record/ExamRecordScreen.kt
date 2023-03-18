package com.mundcode.muntam.presentation.screen.exam_record

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.components.dialogs.JumpNumberPickerDialog
import com.mundcode.designsystem.components.dialogs.alert.AlertDialog
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
import com.mundcode.designsystem.util.spToDp
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.muntam.R
import com.mundcode.muntam.presentation.screen.exam_record.component.BottomButton
import com.mundcode.muntam.presentation.screen.exam_record.component.TimerCircularProgressBar
import com.mundcode.muntam.presentation.screen.exam_record.component.TopExamState
import com.mundcode.muntam.util.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

// todo 타이머 시간 텍스트 고정너비로 덜컹거리지 않게 수정
// todo READY -> RUNNING 에서 덜컹거리지 않게 수정
// todo 중단하고 처음 들어왔을 때 버그 수정
// todo 퍼센트에 따라 원형 프로그레스 바 업데이트 버그 수정
// todo 시스템 버튼으로 뒤로가기, 또는 종료해도 상태 PAUSE 로 만들고 마지막 상태 저장
@Composable
fun ExamRecordScreen(
    viewModel: ExamRecordViewModel = hiltViewModel(),
    onNavEvent: (String) -> Unit,
    onClickBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val examState = state.examModel.state

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collectLatest { route ->
            Log.d("SR-N", "navigationEvent $route")
            onNavEvent(route)
        }
    }

    BackHandler {
        viewModel.onClickBack()
    }

    if (state.confirmBack) {
        onClickBack()
    }

    Scaffold(
        topBar = {
            MTTitleToolbar(
                onClickBack = viewModel::onClickBack,
                title = state.examModel.name,
                icons = listOf {
                    Icon(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 20.dp)
                            .clickable(
                                onClick = viewModel::onClickComplete,
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ),
                        painter = painterResource(id = R.drawable.ic_check_24_dp),
                        contentDescription = null,
                        tint = Gray900
                    )
                }
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
        ExamRecordTimerScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                ),
            backgroundLineColor = when (examState) {
                ExamState.PAUSE -> Gray100
                else -> MTLightOrange
            },
            lineColor = when (examState) {
                ExamState.PAUSE -> Gray300
                else -> MTOrange
            },
            newPercentage = state.percent,
            currentTime = state.currentExamTimeText,
            currentTimeColor = when (examState) {
                ExamState.READY -> Gray500
                else -> Gray900
            },
            remainTime = state.remainExamTimeText,
            remainTimeColor = when (examState) {
                ExamState.READY, ExamState.END -> Gray300
                else -> MTRed

            },
            topStateComposable = {
                TopExamState(
                    emoji = when (examState) {
                        ExamState.READY, ExamState.PAUSE -> "💬"
                        ExamState.RUNNING -> "✍️"
                        ExamState.END -> "👏"
                    },
                    stateContent = {
                        when (examState) {
                            ExamState.READY -> {
                                Text(
                                    text = "시작 대기 중",
                                    style = MTTextStyle.textBold20.spToDp(),
                                    color = Gray700
                                )
                            }
                            ExamState.RUNNING -> {
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
                                    style = MTTextStyle.text20.spToDp(),
                                    color = Gray900
                                )
                            }
                            ExamState.PAUSE -> {
                                Text(
                                    text = "일시 정지 중",
                                    style = MTTextStyle.textBold20.spToDp(),
                                    color = Gray700
                                )
                            }
                            ExamState.END -> {
                                Text(
                                    text = "문제풀이 완료",
                                    style = MTTextStyle.textBold20.spToDp(),
                                    color = MTOrange
                                )
                            }
                        }
                    }
                )
            },
            bottomStateComposable = {
                when (examState) {
                    ExamState.READY -> {
                        Text(
                            text = "화면을 터치하면 기록이 시작됩니다.",
                            style = MTTextStyle.textBold16.spToDp(),
                            color = MTOrange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                    ExamState.RUNNING -> {
                        Text(
                            text = state.currentQuestionTimeText,
                            style = MTTextStyle.textBold16.spToDp(),
                            color = Gray700,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                    ExamState.PAUSE -> {
                        Text(
                            text = "화면을 터치하면 다시 시작됩니다.",
                            style = MTTextStyle.textBold16.spToDp(),
                            color = Gray700,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                    ExamState.END -> {
                        Text(
                            text = "시험 종료! 결과를 확인해보세요!",
                            style = MTTextStyle.textBold16.spToDp(),
                            color = Gray700,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            onClickScreen = viewModel::onClickScreen
        )
    }

    if (state.showCompleteDialog) {
        AlertDialog(
            title = "문제풀이가 끝나셨나요?",
            subtitle = "현재 상태로 최종 시간이 기록됩니다.\n앞으로는 문제 기록 조회만 가능합니다.",
            cancelText = "아니요",
            confirmText = "네, 완료입니다",
            confirmPrimary = true,
            onClickCancel = viewModel::onCancelDialog,
            onClickConfirm = viewModel::onSelectConfirmCompleteDialog
        )
    }

    if (state.showBackConfirmDialog) {
        AlertDialog(
            title = "기록 종료",
            subtitle = "정말 종료하시겠습니까?\n다음에 이어서 기록할 수 있어요.",
            cancelText = "아니요",
            confirmText = "네, 종료할게요",
            confirmPrimary = true,
            onClickCancel = viewModel::onCancelDialog,
            onClickConfirm = viewModel::onSelectConfirmBackDialog
        )
    }

    if (state.showJumpQuestionDialog) {
        state.examModel.lastQuestionNumber?.let { currentQuestionNumber ->
            JumpNumberPickerDialog(
                selectableNumbers = state.selectableNumbers,
                currentNumber = currentQuestionNumber,
                onCancel = viewModel::onCancelDialog,
                onSelect = viewModel::onSelectNumberJumpDialog
            )
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
    Box(
        modifier = modifier.clickable(onClick = onClickScreen),
        contentAlignment = BiasAlignment(horizontalBias = 0f, verticalBias = -(82 / 180f))
    ) {
        Column(
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

            Margin(dp = 12.dp)

            bottomStateComposable()
        }
    }

}

