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
    private val initQuestion: List<QuestionModel>,
    private val onTick: suspend (sec: Long) -> Unit = {},
) {
    private var currentTime: Long = initialTime
    private var remainTime: Long = timeLimit

    private var laps = mutableListOf<Long>()

    private var state: ExamState = ExamState.READY

    private val mutex = Mutex()

    init {
        initializeLapsedQuestion(initQuestion)
    }

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

    private fun initializeLapsedQuestion(questions: List<QuestionModel>) {
        // todo 크기 검증 문제 업데이트시 modifiedAt 넣기
        var lastLapsedQuestion = questions.firstOrNull()
        for (item in questions) {
            val prev = lastLapsedQuestion?.modifiedAt
            val cur = item.modifiedAt
            if (prev != null && cur != null && prev < cur) {
                lastLapsedQuestion = item
            }
        }
        lastLapsedQuestion?.let {
            laps.add(it.lapsedExamTime)
        }
    }

    // todo 초기에 이미 푼 문제 넣는 로직 추가
    fun addCompletedQuestion(question: QuestionModel): QuestionModel {
        val lastLaps = laps.lastOrNull()
        laps.add(currentTime)

        val prevLapsedTime = question.lapsedTime

        return question.copy(
            state = QuestionState.PAUSE,
            lapsedTime = if (lastLaps != null) {
                currentTime - lastLaps + prevLapsedTime
            } else {
                currentTime + prevLapsedTime
            },
            lapsedExamTime = currentTime
        )
    }


    companion object {
        const val DEFAULT_INITIAL_TIME = 0L
    }
}