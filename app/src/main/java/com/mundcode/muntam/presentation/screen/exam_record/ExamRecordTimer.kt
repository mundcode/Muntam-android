package com.mundcode.muntam.presentation.screen.exam_record

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
    private val initExamState: ExamState,
    private val onTick: suspend (current: String, remain: String, currentQuestionTime: String) -> Unit,
) {
    private var currentTime: Long = initialTime
    private var remainTime: Long = (timeLimit / 1000) - initialTime
    private var currentQuestionTime: Long = getLastQuestion(questions = initQuestion)?.lapsedTime?.div(1000) ?: 0
    private var currentQuestion: QuestionModel? = null

    private var state: ExamState = initExamState

    private val mutex = Mutex()

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null

    fun start() {
        if (state == ExamState.RUNNING && job?.isActive == true) return

        job = coroutineScope.launch {
            mutex.withLock {
                state = ExamState.RUNNING
            }

            while (state == ExamState.RUNNING || job?.isActive == true) {
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
        if (state == ExamState.PAUSE) return@launch

        job?.cancel()
        mutex.withLock {
            state = ExamState.PAUSE
        }
    }

    fun end() {
        if (state == ExamState.END) return

        job?.cancel()
        coroutineScope.cancel()
    }

    private fun getLastQuestion(questions: List<QuestionModel>): QuestionModel? {
        var lastLapsedQuestion = questions.firstOrNull()
        for (item in questions) {
            val prev = lastLapsedQuestion?.modifiedAt
            val cur = item.modifiedAt
            if (prev != null && cur != null && prev < cur) {
                lastLapsedQuestion = item
            }
        }
        return lastLapsedQuestion
    }

    suspend fun addCompletedQuestion(question: QuestionModel): QuestionModel {
        val newQuestion = question.copy(
            state = QuestionState.PAUSE,
            lapsedTime = currentQuestionTime,
            lapsedExamTime = currentTime
        )
        mutex.withLock {
            currentQuestionTime = 0
        }

        return newQuestion
    }

    suspend fun setCurrentQuestion(question: QuestionModel) {
        if (currentQuestion == question) return

        mutex.withLock {
            currentQuestionTime = question.lapsedTime
            onTick(
                currentTime.asCurrentTimerText(),
                remainTime.asCurrentTimerText(),
                currentQuestionTime.asCurrentTimerText()
            )
        }
    }

    fun getCurrentTime() = currentTime

    companion object {
        const val DEFAULT_INITIAL_TIME = 0L
    }
}