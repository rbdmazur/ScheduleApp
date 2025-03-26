package com.example.scheduleapp.workermanager

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.scheduleapp.MainActivity
import com.example.scheduleapp.NOTIFICATION_CHANNEL_ID
import com.example.scheduleapp.R
import com.example.scheduleapp.data.model.Notification
import com.example.scheduleapp.data.services.NotificationService
import com.example.scheduleapp.remote.auth.SessionManager
import java.util.Calendar
import java.util.Date
import java.util.UUID

class NotificationWorkerManager(
    private val context: Context,
    workerParameters: WorkerParameters
    ) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val sharedPref = context.applicationContext
            .getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

        val userId = UUID.fromString(sharedPref.getString(SessionManager.USER_ID, null))

        val notificationService = NotificationService()
        val notifications = notificationService.getAllNotifications(userId)
        val closestNotifications = notifications.filter {
            checkDate(Date(it.date))
        }

        Log.d("WORKER", "I'm working, $closestNotifications")
        notifyUser(closestNotifications)
        return Result.success()
    }

    private fun checkDate(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, -24)
        val minTime = calendar.time
        calendar.add(Calendar.HOUR, 48)
        val maxTime = calendar.time

        return date == date.coerceIn(minTime, maxTime)
    }

    private fun notifyUser(list: List<Notification>) {
        val intent = MainActivity.newIntent(context)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val resources = context.resources

        val stringBuilder = StringBuilder()

        list.forEach {
            stringBuilder.append("${it.title}\n")
        }

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(resources.getString(R.string.notification_main_title))
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(stringBuilder.toString()))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(context).notify(0, notification)
    }
}