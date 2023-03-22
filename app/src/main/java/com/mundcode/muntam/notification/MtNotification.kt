package com.mundcode.muntam.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mundcode.muntam.MainActivity
import com.mundcode.muntam.R
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
        val content = "지금 복습하지 않으면 잊어버릴 가능성이 높아요\n얼른 앱을 켜서 복습하고 성적올리기!"

        // todo 문제로 딥링크 태우기
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
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

    companion object {
        const val CHANNEL_ID = "문제 리마인드"
        const val CHANNEL_NAME = "에빙하우스 망각곡선에 입각한 중요 문제 리마인드"
    }
}