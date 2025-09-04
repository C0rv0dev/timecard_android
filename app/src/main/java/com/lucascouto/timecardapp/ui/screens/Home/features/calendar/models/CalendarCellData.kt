package com.lucascouto.timecardapp.ui.screens.Home.features.calendar.models

data class CalendarCellData(
    val day: Int,
    val isToday: Boolean = false,
    val isFaded: Boolean = false,
    val event: CalendarEvent? = null
)
