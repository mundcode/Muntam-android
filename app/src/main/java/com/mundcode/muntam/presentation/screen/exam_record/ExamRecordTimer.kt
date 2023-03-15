package com.mundcode.muntam.presentation.screen.exam_record

import android.util.Log
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.domain.model.enums.QuestionState
import com.mundcode.muntam.presentation.model.QuestionModel
import com.mundcode.muntam.util.asCurrentTimerText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ExamRecordTimer(
    private val initialTime: Long = DEFAULT_INITIAL_TIME,
    private val timeLimit: Long = 0,
    private val initQuestion: List<QuestionModel>,
    private val onTick: suspend (current: String, remain: String, currentQuestionTime: String) -> Unit,
) {
    private var currentTime: Long = initialTime
    private var remainTime: Long = (timeLimit / 1000) - initialTime
    private var currentQuestionTime: Long = getLastQuestion(questions = initQuestion)?.lapsedTime?.div(1000) ?: 0

    private var laps = mutableListOf<Long>()

    private var state: ExamState = ExamState.READY

    private val mutex = Mutex()

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null


    init {
        initializeLapsedQuestion(initQuestion)
    }

    fun start() {
        job = coroutineScope.launch {
            state = ExamState.RUNNING

            while (state == ExamState.RUNNING && job?.isActive == true) {
                delay(1000L)
                mutex.withLock {
                    currentTime += 1
                    currentQuestionTime += 1
                    remainTime -= 1
                    onTick(
                        currentTime.asCurrentTimerText(),
                        remainTime.asCurrentTimerText(),
                        currentQuestionTime.asCurrentTimerText()
                    )
                }
            }
        }
    }

    fun pause() = coroutineScope.launch {
        job?.cancel()
        mutex.withLock {
            state = ExamState.PAUSE
        }
    }

    fun end() {
        job?.cancel()
        coroutineScope.cancel()
    }

    private fun initializeLapsedQuestion(questions: List<QuestionModel>) {
        val lastLapsedQuestion = getLastQuestion(questions)
        lastLapsedQuestion?.let {
            laps.add(it.lapsedExamTime)
        }
    }

    private fun getLastQuestion(questions: List<QuestionModel>): QuestionModel? {
        Log.d("SR-N", "initializeLapsedQuestion size = ${questions.size}")
        var lastLapsedQuestion = questions.firstOrNull()
        for (item in questions) {
            val prev = lastLapsedQuestion?.modifiedAt
            val cur = item.modifiedAt
            if (prev != null && cur != null && prev < cur) {
                lastLapsedQuestion = item
            }
        }
        Log.d("SR-N", "initializeLapsedQuestion questionNumber = ${lastLapsedQuestion?.questionNumber}")
        return lastLapsedQuestion
    }

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

    fun getCurrentTime() = currentTime

    companion object {
        const val DEFAULT_INITIAL_TIME = 0L
    }
}