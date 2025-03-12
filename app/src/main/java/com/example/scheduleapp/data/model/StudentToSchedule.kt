package com.example.scheduleapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import java.util.UUID

@Entity(primaryKeys = ["userId", "scheduleId"])
data class StudentToSchedule(
    val userId: UUID,
    val scheduleId: Int,
    val isMain: Boolean
)

data class StudentWithSchedule(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "userId",
        entityColumn = "scheduleId",
        associateBy = Junction(StudentToSchedule::class)
    )
    val schedules: List<Schedule>
)
