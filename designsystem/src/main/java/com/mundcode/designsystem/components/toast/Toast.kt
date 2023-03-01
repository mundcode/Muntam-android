package com.mundcode.designsystem.components.toast

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.CornerRadius8
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.MTToastBackground
import com.mundcode.designsystem.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicBoolean

// internal constructor 직접 생성하지 못하게 막음
fun rememberToastState(duration: Long = 2000, interDuration: Long = 300) = ToastState(duration, interDuration)

class ToastState internal constructor(
    private val duration: Long,
    private val interDuration: Long
) {
    var show: Boolean by mutableStateOf(false)
        private set

    val mutex = Mutex()

    suspend fun showToast() {
        mutex.withLock {
            show = true
            delay(duration)
            show = false
            delay(interDuration)
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomToast(
    toastState: ToastState,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = toastState.show,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        content()
    }
}

@Composable
fun CustomToastTest(
    show: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = show
    ) {
        content()
    }
}


@Composable
fun MTToast(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MTTextStyle.text14,
        color = White,
        textAlign = TextAlign.Center,
        modifier = modifier
            .background(color = MTToastBackground, shape = CornerRadius8)
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    )
}

@Preview
@Composable
fun MTToastPreview() {
    MTToast(text = "새로운 과목이 추가되었습니다.", modifier = Modifier.padding(20.dp))
}

@Preview
@Composable
fun CustomToastPreview() {
    val toastState by remember {
        mutableStateOf(rememberToastState())
    }

    LaunchedEffect(key1 = Unit) {
        delay(1000)
        toastState.showToast()
    }

    CustomToast(toastState = toastState) {
        MTToast(text = "새로운 과목이 추가되었습니다.", modifier = Modifier.padding(20.dp))
    }
}