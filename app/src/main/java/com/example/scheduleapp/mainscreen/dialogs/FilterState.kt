package com.example.scheduleapp.mainscreen.dialogs

import com.example.scheduleapp.data.model.Faculty

data class FilterState(
    val faculty: Faculty? = null,
    val course: Int? = null,
    val group: Int? = null,
    val isFilterClicked: Boolean = false
)
