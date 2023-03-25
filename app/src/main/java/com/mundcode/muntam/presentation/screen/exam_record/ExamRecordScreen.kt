package com.mundcode.muntam.presentation.screen.exam_record

import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.Lifecycle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.mundcode.designsystem.components.dialogs.JumpNumberPickerDialog
import com.mundcode.designsystem.components.dialogs.alert.AlertDialog
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.components.toast.MTToast
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
import com.mundcode.muntam.util.ActivityLifecycle
import com.mundcode.muntam.util.getActivity
import com.mundcode.muntam.util.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ExamRecordScreen(
    viewModel: ExamRecordViewModel = hiltViewModel(),
    onNavEvent: (String) -> Unit,
    onBackEvent: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val examState = state.examModel.state
    val isNotEnd = state.examModel.state != ExamState.END

    var showAdLoading by remember {
        mutableStateOf(false)
    }

    val activity = getActivity()

    var rewardedAd by remember {
        mutableStateOf<RewardedAd?>(null)
    }

    val prevPercentage by remember {
        mutableStateOf(0f)
    }

    ActivityLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                viewModel.onDispose()
            }
        }
    }

    LaunchedEffect(key1 = true) {
        launch(Dispatchers.Main) {
            loadAdRequest(
                activity,
                onRewardedCallback = {
                    rewardedAd = it
                },
                onLoadFailedEvent = {}
            )
        }

        launch(Dispatchers.Main) {
            viewModel.showAdEvent.collectLatest {
                rewardedAd?.show(activity) {
                    viewModel.onCompleteAdmob()
                }
                if (rewardedAd == null) {
                    viewModel.onAdLoadFailed()

                    showAdLoading = true
                    loadAdRequest(
                        activity,
                        onRewardedCallback = {
                            showAdLoading = false
                            viewModel.onAdLoadComplete()

                            rewardedAd = it
                        },
                        onLoadFailedEvent = {
                            showAdLoading = false
                            viewModel.onAdLoadFailed()
                        }
                    )
                }
            }
        }

        launch {
            viewModel.navigationEvent.collectLatest { route ->
                onNavEvent(route)
            }
        }

        launch {
            viewModel.toast.collectLatest {
                viewModel.toastState.showToast(it)
            }
        }
    }

    DisposableEffect(key1 = true) {
        onDispose {
            rewardedAd = null
        }
    }

    BackHandler {
        if (isNotEnd) {
            viewModel.onClickBack()
        }
    }

    if (state.confirmBack) {
        if (isNotEnd) {
            onBackEvent()
        }
    }

    Scaffold(
        topBar = {
            MTTitleToolbar(
                onClickBack = viewModel::onClickBack,
                backEnabled = isNotEnd,
                title = state.examModel.name,
                icons = listOf {
                    Icon(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 20.dp)
                            .clickable(
                                onClick = viewModel::onClickComplete,
                                indication = null,
                                interactionSource = MutableInteractionSource(),
                                enabled = isNotEnd
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
                    enabled = state.examModel.state == ExamState.RUNNING && isNotEnd,
                    onClick = viewModel::onClickPause,
                )

                BottomButton(
                    resId = R.drawable.ic_skip_56_dp,
                    text = "문제 건너뛰기",
                    enabled = state.examModel.state == ExamState.RUNNING && isNotEnd,
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
            lineColor = when {
                examState == ExamState.PAUSE -> Gray300
                state.expired -> MTRed
                else -> MTOrange
            },
            prevPercentage = prevPercentage,
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
                                .padding(horizontal = 20.dp),
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
                                .padding(horizontal = 20.dp),
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                    ExamState.END -> {
                        Text(
                            text = if (state.examModel.completeAd) {
                                "한 번 더 터치하고 시험 결과를 확인해보세요!"
                            } else {
                                "수고했어요\n한 번 더 터치해서 광고를 보며 머리를 식히고,\n시험결과를 확인하세요!"
                            },
                            style = MTTextStyle.textBold16.spToDp(),
                            color = Gray700,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            expired = state.expired,
            onClickScreen = viewModel::onClickScreen
        )

        if (showAdLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MTOrange, strokeWidth = 4.dp)
            }
        }
    }

    if (viewModel.toastState.show) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            MTToast(
                toastState = viewModel.toastState, modifier = Modifier
                    .padding(20.dp)
            )
        }
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
            confirmPrimary = false,
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

private fun loadAdRequest(
    activity: ComponentActivity,
    onRewardedCallback: (RewardedAd) -> Unit,
    onLoadFailedEvent: () -> Unit
) {
    var adRequest = AdRequest.Builder().build()
    RewardedAd.load(
        activity,
        "ca-app-pub-3940256099942544/5224354917",
        adRequest,
        object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                onRewardedCallback(ad)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                onLoadFailedEvent()
            }
        }
    )
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
    expired: Boolean,
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
                remainTimeColor = remainTimeColor,
                expired = expired
            )

            Margin(dp = 24.dp)

            bottomStateComposable()
        }
    }
}
