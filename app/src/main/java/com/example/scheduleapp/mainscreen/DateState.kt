package com.example.scheduleapp.mainscreen

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DateState(
    val dates: List<Date> = emptyList(),
    val selectedDay: Int = -1
)

object DateFormat {
    val barTitleFormat = SimpleDateFormat("MMMM d, yyyy", Locale.US)
    val dateRowFormat = SimpleDateFormat("dd, E", Locale.US)
}
