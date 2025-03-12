package com.example.scheduleapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "student")
data class Student(
    @PrimaryKey val userId: UUID,
    val name: String,
    val infoId: Int
)
