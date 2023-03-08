package com.mundcode.muntam.presentation.screen.exam_record

import com.mundcode.domain.model.enums.ExamState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ExamRecordTimer(
    private val initialTime: Long = DEFAULT_INITIAL_TIME,
    private val timeLimit: Long = 0,
    private val scope: CoroutineScope,
    private val onTick: suspend (sec: Long) -> Unit,
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
                onTick(currentTime)
            }
        }
    }

    fun pause() = scope.launch {
        mutex.withLock {
            state = ExamState.PAUSE
        }
    }

    companion object {
        const val DEFAULT_INITIAL_TIME = 0L
    }
}