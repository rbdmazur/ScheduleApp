package com.example.scheduleapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.scheduleapp.data.repository.AppRepository
import dagger.hilt.android.HiltAndroidApp

const val NOTIFICATION_CHANNEL_ID = "notif_poll"

@HiltAndroidApp
class ScheduleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppRepository.init(this.applicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}