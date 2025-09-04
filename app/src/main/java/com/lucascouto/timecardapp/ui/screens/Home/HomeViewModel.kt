package com.lucascouto.timecardapp.ui.screens.Home

import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.CalendarState

class HomeViewModel() {
    private val _calendarState = CalendarState()
    val calendarState by lazy { _calendarState }

    private val _estimatedSalary = 123456
    val estimatedSalary by lazy { _estimatedSalary }
}
