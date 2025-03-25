package com.example.scheduleapp.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormats {
    val barTitleFormat = SimpleDateFormat("MMMM d, yyyy", Locale.US)
    val dateRowFormat = SimpleDateFormat("dd, E", Locale.US)
    val notificationDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
}