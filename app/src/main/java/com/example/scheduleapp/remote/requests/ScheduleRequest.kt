package com.example.scheduleapp.remote.requests

data class ScheduleRequest(
    val scheduleId: String,
    val isMain: Boolean
)

data class ScheduleRequestList(
    val list: List<ScheduleRequest>
)