package com.mundcode.designsystem.components.toast

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ToastState(
    private val duration: Long = 2000
) {
    var show: Boolean = false

    suspend fun showToast() {
        CoroutineScope(Dispatchers.Default).launch {
            show = true
            delay(duration)
            show = false
        }
    }
}