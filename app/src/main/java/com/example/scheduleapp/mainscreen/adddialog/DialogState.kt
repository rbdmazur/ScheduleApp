package com.example.scheduleapp.mainscreen.adddialog

import com.example.scheduleapp.data.model.Schedule

data class DialogState(
    val list: List<ScheduleLazyColumnItem> = emptyList()
)

data class ScheduleLazyColumnItem(
    val schedule: Schedule,
    val isSelected: Boolean = false
)
