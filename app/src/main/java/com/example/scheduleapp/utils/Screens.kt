package com.example.scheduleapp.utils

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
object LoginScreenRoute

@Serializable
data class MainScreenRoute(
    val id: String
)


@Serializable
object ScheduleScreenRoute