package com.mundcode.muntam.presentation.screen.exam_record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.components.toolbars.MTTitleToolbar
import com.mundcode.designsystem.theme.Gray300
import com.mundcode.designsystem.theme.Gray500
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTLightOrange
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTRed
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.muntam.R
import com.mundcode.muntam.presentation.screen.exam_record.component.BottomButton
import com.mundcode.muntam.presentation.screen.exam_record.component.TimerCircularProgressBar
import com.mundcode.muntam.util.hiltViewModel

@Composable
fun ExamRecordScreen(
    viewModel: ExamRecordViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            MTTitleToolbar(
                onClickBack = viewModel::onClickBack,
                title = state.examModel.name,
                icons = listOf(
                    {
                        Button(onClick = viewModel::onClickSetting) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_setttings_24_dp),
                                contentDescription = null,
                                tint = Gray900
                            )
                        }
                    },
                    {
                        Button(onClick = viewModel::onClickComplete) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check_24_dp),
                                contentDescription = null,
                                tint = Gray900
                            )
                        }
                    }
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 42.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomButton(
                    resId = R.drawable.ic_pause_off_56_dp,
                    text = "일시 정지",
                    enable = state.examModel.state != ExamState.RUNNING,
                    onClick = viewModel::onClickPause
                )

                BottomButton(
                    resId = R.drawable.ic_skip_56_dp,
                    text = "문제 건너뛰기",
                    enable = state.examModel.state != ExamState.RUNNING,
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
                                         
                    },
                    bottomStateComposable = {

                    }
                )
            }
            ExamState.RUNNING -> {
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

                    },
                    bottomStateComposable = {

                    }
                )
            }
            ExamState.PAUSE -> {
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

                    },
                    bottomStateComposable = {

                    }
                )
            }
            ExamState.END -> {
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

                    },
                    bottomStateComposable = {

                    }
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
    topStateComposable: @Composable () -> Unit = {},
    bottomStateComposable: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        topStateComposable()

        TimerCircularProgressBar(
            backgroundLineColor = backgroundLineColor,
            lineColor = lineColor,
            prevPercentage = prevPercentage,
            newPercentage = newPercentage,
            currentTime = currentTime,
            currentTimeColor = currentTimeColor,
            remainTime = remainTime,
            remainTimeColor = remainTimeColor
        )

        bottomStateComposable()
    }
}

