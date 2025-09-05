package com.lucascouto.timecardapp.ui.screens.Home.features.calendar.models

import java.time.LocalDate

data class CalendarCellData(
    val date: LocalDate,
    val isToday: Boolean = false,
    val isFaded: Boolean = false,
    val event: CalendarEvent? = null
)
