package com.example.scheduleapp.mainscreen

import java.util.Date

data class DateState(
    val dates: List<Date> = emptyList(),
    val selectedDay: Int = -1
)
