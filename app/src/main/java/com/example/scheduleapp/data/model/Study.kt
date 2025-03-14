package com.example.scheduleapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity
data class Study(
    @PrimaryKey val id: Int,
    val subjectId: Int,
    val day: String,
    val number: Int,
    val time: String,
    val type: String,
    val auditorium: String,
    val teacherId: UUID,
    val scheduleId: Int
)

data class StudyWithTeacherAndSubject(
    @Embedded val study: Study,
    @Relation(
        parentColumn = "subjectId",
        entityColumn = "id"
    )
    val subject: Subject,
    @Relation(
        parentColumn = "teacherId",
        entityColumn = "userId"
    )
    val teacher: Teacher
)


