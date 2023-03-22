package com.mundcode.muntam.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mundcode.domain.usecase.GetQuestionByQuestionIdUseCase
import com.mundcode.muntam.notification.MtNotification
import com.mundcode.muntam.presentation.model.asStateModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope

@HiltWorker
class QuestionNotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val getQuestionByQuestionIdUseCase: GetQuestionByQuestionIdUseCase,
    val mtNotification: MtNotification
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return coroutineScope {
            val questionId: Int = inputData.getInt(PARAM_QUESTION_ID, 0)
            val howLong: String = inputData.getString(PARAM_HOW_LONG) ?: ""

            val question = getQuestionByQuestionIdUseCase(questionId).asStateModel()

            mtNotification.showNotification(
                applicationContext,
                id = question.hashCode(),
                builder = mtNotification.createQuestionNotification(
                    context = applicationContext,
                    questionModel = question,
                    howLong = howLong
                )
            )

            Result.success()
        }
    }

    companion object {
        const val WORKER_ID = "Question notification tag %d"
        const val PARAM_QUESTION_ID = "question_id"
        const val PARAM_HOW_LONG = "how_long"

        fun getWorkerIdWithArgs(questionId: Int): String {
            return WORKER_ID.format(questionId)
        }
    }
}
