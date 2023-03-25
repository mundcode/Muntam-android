package com.mundcode.muntam.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.mundcode.muntam.MainActivity
import com.mundcode.muntam.R
import com.mundcode.muntam.constants.URI
import com.mundcode.muntam.navigation.Questions
import com.mundcode.muntam.presentation.model.QuestionModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MtNotification @Inject constructor() {
    fun createQuestionNotification(
        context: Context,
        questionModel: QuestionModel,
        howLong: String
    ): NotificationCompat.Builder {
        val title = "${questionModel.questionNumber}번 문제를 푼지 $howLong 지났어요."
        val content = "지금 복습하지 않으면 잊어버릴 가능성이 높아요\n얼른 복습하고 성적올리기!"

        // Targeting S+ (version 31 and above) requires that one of FLAG_IMMUTABLE or FLAG_MUTABLE
        // be specified when creating a PendingIntent.
        // Strongly consider using FLAG_IMMUTABLE, only use FLAG_MUTABLE
        // if some functionality depends on the PendingIntent being mutable, e.g. if it needs to be used with inline replies or bubbles.
        val deepLinkPendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            val deepLinkIntent = Intent(
                Intent.ACTION_VIEW,
                "$URI${Questions.getRouteWithArgs(questionModel.examId)}".toUri(),
                context,
                MainActivity::class.java
            )
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        } ?: run {
            getDefaultPendingIntent(context)
        }


        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(deepLinkPendingIntent)
            .setAutoCancel(true)
    }

    fun showNotification(context: Context, id: Int = 0, builder: NotificationCompat.Builder) =
        with(NotificationManagerCompat.from(context)) {
            notify(id, builder.build())
        }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_ID
            val descriptionText = CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getDefaultPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    companion object {
        const val CHANNEL_ID = "문제 리마인드"
        const val CHANNEL_NAME = "에빙하우스 망각곡선에 입각한 중요 문제 리마인드"
    }
}
