package com.mundcode.designsystem.components.dialogs.picker

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.mundcode.designsystem.components.dialogs.ContentDialog
import com.mundcode.designsystem.theme.CornerRadius12
import com.mundcode.designsystem.theme.Gray500
import com.mundcode.designsystem.theme.MTLightOrange
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.util.spToDp

const val Unit24 = 24
const val Unit60 = 60

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TimeLimitPickerDialog(
    onResult: (hour: Int, min: Int, sec: Int) -> Unit,
    onCancel: () -> Unit
) {
    val hourState = rememberPagerState()
    val minState = rememberPagerState()
    val secState = rememberPagerState()

    ContentDialog(
        title = "제한시간 설정",
        onClickClose = onCancel,
        onClickCancel = onCancel,
        onClickConfirm = {
            Log.d("SR-N", "${hourState.currentPage} / ${minState.currentPage} / ${secState.currentPage}")

            onResult(
                hourState.currentPage % Unit24,
                minState.currentPage % Unit60,
                secState.currentPage % Unit60
            )
        }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(74.dp)
                    .background(color = MTLightOrange, shape = CornerRadius12)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                VerticalScrollNumberPicker(
                    count = 24,
                    unit = "시",
                    pagerState = hourState
                )
                VerticalScrollNumberPicker(
                    count = 60,
                    unit = "분",
                    pagerState = minState
                )
                VerticalScrollNumberPicker(
                    count = 60,
                    unit = "초",
                    pagerState = secState
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun VerticalScrollNumberPicker(
    count: Int,
    unit: String,
    pagerState: PagerState = rememberPagerState()

) {
    val currentPage = pagerState.currentPage

    LaunchedEffect(key1 = true) {
        var half = Int.MAX_VALUE / 2
        while (half % count != 0) {
            half += 1
        }
        pagerState.scrollToPage(half)
    }

    Box(
        modifier = Modifier
            .height(186.dp)
            .width(80.dp)
    ) {
        Text(
            text = unit,
            style = MTTextStyle.text13.spToDp(),
            color = MTOrange,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(
                    Alignment.Center
                )
                .offset(y = 12.dp)
        )
        VerticalPager(
            count = Int.MAX_VALUE,
            contentPadding = PaddingValues(vertical = 56.dp),
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) { index ->
            val isSelected = currentPage == index
            Text(
                text = "%02d".format(index % count),
                style = if (isSelected) MTTextStyle.textBold20.spToDp() else MTTextStyle.text20.spToDp(),
                color = if (isSelected) MTOrange else Gray500,
                modifier = if (isSelected) Modifier.offset(y = (-9).dp) else Modifier
            )
        }
    }
}

@Preview
@Composable
fun VerticalScrollNumberPickerPreview() {
    TimeLimitPickerDialog({ a, b, c -> }, {})
}
