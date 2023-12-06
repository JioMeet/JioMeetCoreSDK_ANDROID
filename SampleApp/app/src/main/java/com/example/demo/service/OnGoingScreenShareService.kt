package com.example.demo.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import com.example.demo.service.ForegroundServiceNotification.createNotification
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random

@AndroidEntryPoint
class OnGoingScreenShareService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_NOT_STICKY
        /*
         * To avoid security exception this code is added inside onCreate method, so that app is not get
         * crashed due to security reasons.
         */
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                startForeground(
                    Random().nextInt(),
                    createNotification(
                        this,
                        intent.getStringExtra("notification_text"),
                        ""
                    ),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                startForeground(
                    Random().nextInt(),createNotification(
                        this,
                        intent.getStringExtra("notification_text"),
                        ""
                    )
                )
            }
        }
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }
}