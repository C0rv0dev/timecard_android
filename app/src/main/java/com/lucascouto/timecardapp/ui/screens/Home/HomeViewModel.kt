package com.lucascouto.timecardapp.ui.screens.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascouto.timecardapp.struct.data.DatabaseProvider
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity
import com.lucascouto.timecardapp.struct.data.enums.WorkdayTypeEnum
import com.lucascouto.timecardapp.struct.data.repositories.WorkdayRepository
import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.CalendarState
import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.models.CalendarEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val workdayRepository: WorkdayRepository = DatabaseProvider.workdayRepository) :
    ViewModel() {
    // Calendar
    private val _calendarState = CalendarState()
    val calendarState by lazy { _calendarState }

    // Workdays
    private lateinit var _workdays: List<WorkdayEntity>

    // Estimated Salary
    private var _estimatedSalary = 123456
    val estimatedSalary by lazy { _estimatedSalary }

    // Total worked hours
    private var _totalWorkedHours = 160
    val totalWorkedHours by lazy { _totalWorkedHours }

    // Total worked days
    private var _totalWorkedDays = 20
    val totalWorkedDays by lazy { _totalWorkedDays }

    // Total overtime hours
    private var _totalOvertimeHours = 10
    val totalOvertimeHours by lazy { _totalOvertimeHours }

    // Init
    init {
        _calendarState.onNextMonth = { refreshData() }
        _calendarState.onPreviousMonth = { refreshData() }
    }

    // Methods
    fun refreshData() {
        viewModelScope.launch {
            _workdays = withContext(Dispatchers.Default) {
                workdayRepository.fetchByMonth(_calendarState.yearMonth())
            }.also { workdays ->
                // Events
                val events: List<CalendarEvent> = workdays.map {
                    CalendarEvent(
                        date = it.getLocalDate(),
                        color = WorkdayTypeEnum.color(it.shiftType),
                    )
                }

                _calendarState.setEvents(events)
                _totalWorkedDays = workdays.size
            }
        }
    }
}
