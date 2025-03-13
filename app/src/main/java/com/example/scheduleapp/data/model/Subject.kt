package com.example.scheduleapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Subject(
    @PrimaryKey
    val id: Int,
    val title: String,
    val shortTitle: String,
    val infoId: Int
)
