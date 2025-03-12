package com.example.scheduleapp.mainscreen

import com.example.scheduleapp.data.model.Schedule
import com.example.scheduleapp.data.model.Student
import com.example.scheduleapp.utils.DaysOfWeek
import java.util.Calendar
import java.util.Date

data class MainUiState(
    val isLoading: Boolean = false,
    val currentStudent: Student? = null,
    val currentSchedule: Int = -1,
    val usersSchedules: List<Schedule> = emptyList(),
    val mainScheduleId: Int = -1,
    val currentDate: Date = Calendar.getInstance().time,
    val currentDay: DaysOfWeek = convertToLocalDaysOfWeek(Calendar.DAY_OF_WEEK),
)

private fun convertToLocalDaysOfWeek(day: Int): DaysOfWeek {
    return when(day) {
        2 -> DaysOfWeek.MONDAY
        3 -> DaysOfWeek.TUESDAY
        4 -> DaysOfWeek.WEDNESDAY
        5 -> DaysOfWeek.THURSDAY
        6 -> DaysOfWeek.FRIDAY
        else -> DaysOfWeek.SATURDAY
    }
}