package com.mundcode.designsystem.components.bottomsheets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.R
import com.mundcode.designsystem.components.buttons.PrimaryMTButton
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.theme.Gray700
import com.mundcode.designsystem.theme.MTBottomSheetBackground
import com.mundcode.designsystem.theme.MTRed
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.TopCornerRadius12
import com.mundcode.designsystem.theme.Transparent
import com.mundcode.designsystem.theme.White

@Composable
fun MTBottomSheets(
    show: Boolean,
    modifier: Modifier = Modifier,
    onClickOutSide: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val height = LocalConfiguration.current.screenHeightDp
    val density = LocalDensity.current

    val backgroundColor by animateColorAsState(
        targetValue = if (show) {
            MTBottomSheetBackground
        } else {
            Transparent
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {
        AnimatedVisibility(
            visible = show,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter),
            enter = slideInVertically(
                animationSpec = spring(
                    dampingRatio = DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) { contentHeight ->
                with(density) {
                    height.dp.roundToPx() + contentHeight
                }
            },
            exit = slideOutVertically { _ ->
                with(density) {
                    height.dp.roundToPx()
                }
            }
        ) {
            content()
        }
    }

}

@Composable
fun SubjectOptionBottomSheet(
    onClickClose: () -> Unit,
    onClickDelete: () -> Unit,
    onClickModify: () -> Unit
) {
    BottomSheetContent(title = "과목 리스트 편집", onClickClose = onClickClose) {
        Column {
            OptionText(
                text = "삭제하기",
                textColor = MTRed,
                onClick = onClickDelete
            )
            OptionText(
                text = "수정하기",
                onClick = onClickModify
            )
        }
    }
}

@Composable
fun BottomSheetContent(
    title: String,
    onClickClose: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = White, shape = TopCornerRadius12)
            .padding(top = 24.dp, bottom = 34.dp),
    ) {
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = title,
                style = MTTextStyle.textBold20,
                modifier = Modifier.weight(1f)
            )
            Margin(dp = 16.dp)
            Icon(
                painter = painterResource(id = R.drawable.ic_close_24_dp),
                contentDescription = null,
                modifier = Modifier.clickable(onClick = onClickClose)
            )
        }
        content()
    }
}

@Composable
fun OptionText(
    text: String,
    textColor: Color = Gray700,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = MTTextStyle.text16,
        color = textColor,
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    )
}

@Preview
@Composable
fun SubjectOptionBottomSheetPreview() {
    SubjectOptionBottomSheet({}, {}, {})
}

@Preview
@Composable
fun MTBottomSheetsPreview() {
    var show by remember {
        mutableStateOf(false)
    }

    PrimaryMTButton(
        text = "보여주기", onClick = { show = true }
    )

    MTBottomSheets(
        show = show,
        onClickOutSide = { show = false }
    ) {
        SubjectOptionBottomSheet({ show = false }, { show = false }, { show = false })
    }
}