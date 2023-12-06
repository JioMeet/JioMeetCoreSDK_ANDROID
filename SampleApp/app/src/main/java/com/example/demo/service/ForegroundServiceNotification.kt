package com.example.demo.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.jio.sdksampleapp.R

object ForegroundServiceNotification {

    fun createNotification(
        context: Context,
        message: String?,
        content_title: String?
    ): Notification {
        val builder =
            NotificationCompat.Builder(context, "JioMeetSDK notification channel")
        createChannel(context)
            builder.setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )
            builder.setSmallIcon(R.drawable.ic_screen_share)

        builder.setContentTitle(content_title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setChannelId("JioMeetSDK notification channel")
            .setAutoCancel(true)
        return builder.build()
    }

    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                "JioMeetSDK notification channel",
                "Sample App",
                importance
            )
            mChannel.setSound(null, null)
            mChannel.enableVibration(false)
            mChannel.enableLights(false)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}