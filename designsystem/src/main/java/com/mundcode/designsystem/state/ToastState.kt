package com.mundcode.designsystem.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// internal constructor 직접 생성하지 못하게 막음
class ToastState internal constructor(
    private val duration: Long,
    private val interDuration: Long
) {
    var show: Boolean by mutableStateOf(false)
        private set

    var text: String by mutableStateOf("")
        private set

    private val mutex = Mutex()

    suspend fun showToast(text: String) {
        mutex.withLock {
            this.text = text
            show = true
            delay(duration)
            show = false
            delay(interDuration)
        }
    }
}

fun rememberToastState(duration: Long = 2000, interDuration: Long = 300) = ToastState(duration, interDuration)
