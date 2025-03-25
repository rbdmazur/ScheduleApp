package com.example.scheduleapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val studentId: UUID,
    val subjectId: Int,
    val studyId: Int? = null,
    val title: String,
    val description: String?,
    val date: Long
)

data class NotificationData(
    val studentId: UUID,
    val subjectId: Int,
    val studyId: Int? = null,
    val title: String,
    val description: String?,
    val date: Long
)
