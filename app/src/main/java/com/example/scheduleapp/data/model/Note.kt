package com.example.scheduleapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val userId: UUID,
    val subjectId: Int,
    val date: Long,
    val title: String,
    val description: String
)
