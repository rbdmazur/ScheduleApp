package com.example.scheduleapp.data.services

import com.example.scheduleapp.data.model.Notification
import com.example.scheduleapp.data.model.NotificationData
import com.example.scheduleapp.data.repository.AppRepository
import java.util.UUID

class NotificationService {
    private val repository = AppRepository.get()

    suspend fun getAllNotifications(userId: UUID): List<Notification> =
        repository.getAllNotifications(userId)
    suspend fun getNotificationById(id: Int): Notification? =
        repository.getNotificationById(id)
    suspend fun insertNotification(notificationData: NotificationData) {
        repository.insertNotification(notificationData)
    }
    suspend fun deleteNotification(notification: Notification) {
        repository.deleteNotification(notification)
    }
}