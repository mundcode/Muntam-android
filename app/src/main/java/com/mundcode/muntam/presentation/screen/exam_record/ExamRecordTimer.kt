package com.mundcode.muntam.presentation.screen.exam_record

import com.mundcode.domain.model.enums.ExamState
import com.mundcode.domain.model.enums.QuestionState
import com.mundcode.muntam.presentation.model.QuestionModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ExamRecordTimer(
    private val initialTime: Long = DEFAULT_INITIAL_TIME,
    private val timeLimit: Long = 0,
    private val scope: CoroutineScope,
    private val onTick: suspend (sec: Long) -> Unit = {},
) {
    private var currentTime: Long = initialTime
    private var remainTime: Long = timeLimit

    private var laps = mutableListOf<Long>()

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

    // todo 초기에 이미 푼 문제 넣는 로직 추가
    fun addCompletedQuestion(question: QuestionModel): QuestionModel {
        val lastLaps = laps.lastOrNull()
        laps.add(currentTime)

        val prevLapsedTime = question.lapsedTime
        val newQuestion = lastLaps?.let {
            question.copy(
                state = QuestionState.PAUSE,
                lapsedTime = currentTime - it + prevLapsedTime,
                lapsedExamTime = currentTime
            )
        } ?: run {
            question.copy(
                state = QuestionState.PAUSE,
                lapsedTime = currentTime + prevLapsedTime,
                lapsedExamTime = currentTime
            )
        }
        return newQuestion
    }


    companion object {
        const val DEFAULT_INITIAL_TIME = 0L
    }
}