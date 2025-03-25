package com.example.scheduleapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.scheduleapp.data.model.Notification
import com.example.scheduleapp.data.model.NotificationData
import java.util.UUID

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notification WHERE studentId=(:userId)")
    suspend fun getAllNotifications(userId: UUID): List<Notification>

    @Query("SELECT * FROM notification WHERE id=(:id)")
    suspend fun getNotificationById(id: Int): Notification?

    @Insert(entity = Notification::class)
    suspend fun insertNotification(notificationData: NotificationData)

    @Delete
    suspend fun deleteNotification(notification: Notification)
}