package com.example.scheduleapp.utils

import kotlinx.serialization.Serializable

@Serializable
object LoginScreenRoute

@Serializable
data class MainScreenRoute(
    val id: String
)

@Serializable
object ScheduleScreenRoute

@Serializable
object SubjectsScreenRoute

@Serializable
object ProfileScreenRoute