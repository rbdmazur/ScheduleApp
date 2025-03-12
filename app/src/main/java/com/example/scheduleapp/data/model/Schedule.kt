package com.example.scheduleapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey val scheduleId: Int,
    val title: String,
    val lastUpdate: Long,
    val infoId: Int
)
