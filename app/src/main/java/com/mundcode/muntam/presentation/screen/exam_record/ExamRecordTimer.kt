package com.mundcode.muntam.presentation.screen.exam_record

import com.mundcode.domain.model.enums.ExamState
import com.mundcode.muntam.util.asTimeLimitText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ExamRecordTimer(
    private val initialTime: Long = 0,
    private val timeLimit: Long = 0,
    private val scope: CoroutineScope
) {
    private var currentTime: Long = initialTime
    private var remainTime: Long = timeLimit

    private var state: ExamState = ExamState.READY

    private val mutex = Mutex()

    fun start() = scope.launch {
        mutex.withLock {
            state = ExamState.RUNNING
        }

        while (state == ExamState.RUNNING) {
            delay(1000L)
            mutex.withLock {
                currentTime += 1
                remainTime -= 1
            }
        }
    }

    fun pause() = scope.launch {
        mutex.withLock {
            state = ExamState.PAUSE
        }
    }

    fun getCurrentTimeText(): String = currentTime.asTimeLimitText()

    fun getRemainTimeText(): String = remainTime.asTimeLimitText()
}