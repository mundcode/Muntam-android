package com.mundcode.designsystem.components.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.mundcode.designsystem.state.ToastState

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
