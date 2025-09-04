package com.lucascouto.timecardapp.ui.screens.Home

import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.CalendarState

class HomeViewModel() {
    private val _calendarState = CalendarState()
    val calendarState by lazy { _calendarState }
}
