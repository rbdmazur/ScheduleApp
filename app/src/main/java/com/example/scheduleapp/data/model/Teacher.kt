package com.example.scheduleapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Teacher(
    @PrimaryKey val userId: UUID,
    val name: String,
    val academicTitle: String,
    val facultyId: Int
)
