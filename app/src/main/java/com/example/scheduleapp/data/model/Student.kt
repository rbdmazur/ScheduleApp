package com.example.scheduleapp.data.model

import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "student")
data class Student(
    val userId: UUID,
    val name: String,
    val facultyId: Int,
    val specialization: String,
    val course: Int,
    val group: Int
)
