package com.example.scheduleapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity(tableName = "student")
data class Student(
    @PrimaryKey val userId: UUID,
    val name: String,
    val infoId: Int
)

data class StudentWithEmailAndInfo(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val user: User,
    @Relation(
        parentColumn = "infoId",
        entityColumn = "id"
    )
    val info: Info
)


