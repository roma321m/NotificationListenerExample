package com.roman.notificationlistenerexample.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.roman.notificationlistenerexample.R
import com.roman.notificationlistenerexample.activity.MainActivity
import kotlin.random.Random

class NotificationService(
    private val context: Context
) : Notification {

    companion object {
        const val CHANNEL_ID = "my_channel"
    }

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showNotification() {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_android)
            .setContentTitle("Notification Title")
            .setContentText("Notification text will be here")
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }
}