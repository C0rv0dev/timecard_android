package com.lucascouto.timecardapp.ui.screens.Home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascouto.timecardapp.struct.data.DatabaseProvider
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity
import com.lucascouto.timecardapp.struct.data.enums.WorkdayTypeEnum
import com.lucascouto.timecardapp.struct.data.logic.WorkdaysDataLogic
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
    private var _estimatedSalary: MutableState<Int?> = mutableStateOf(null)
    val estimatedSalary: State<Int?> by lazy { _estimatedSalary }

    // Total worked hours
    private var _totalWorkedHours: MutableState<Int?> = mutableStateOf(null)
    val totalWorkedHours: State<Int?> by lazy { _totalWorkedHours }

    // Total worked days
    private var _totalWorkedDays: MutableState<Int?> = mutableStateOf(null)
    val totalWorkedDays: State<Int?> by lazy { _totalWorkedDays }

    // Total overtime hours
    private var _totalOvertimeHours: MutableState<Int?> = mutableStateOf(null)
    val totalOvertimeHours: State<Int?> by lazy { _totalOvertimeHours }

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
                val workdaysDataLogic = WorkdaysDataLogic(workdays)

                // Events
                val events: List<CalendarEvent> = workdays.map {
                    CalendarEvent(
                        date = it.getLocalDate(),
                        color = WorkdayTypeEnum.color(it.shiftType),
                    )
                }

                _calendarState.setEvents(events)

                _estimatedSalary.value = workdaysDataLogic.calculateEstimatedSalary()
                _totalWorkedHours.value = workdaysDataLogic.calculateTotalWorkedHours()
                _totalWorkedDays.value = workdaysDataLogic.calculateTotalWorkedDays()
                _totalOvertimeHours.value = workdaysDataLogic.calculateTotalOvertimeHours()
            }
        }
    }
}
