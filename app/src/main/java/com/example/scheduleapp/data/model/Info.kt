package com.example.scheduleapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "info")
data class Info(
    @PrimaryKey val id: Int,
    val facultyId: Int,
    val specialization: String,
    val course: String,
    val group: String
)
