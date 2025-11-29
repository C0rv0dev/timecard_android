package com.lucascouto.timecardapp.ui.screens.Home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascouto.timecardapp.struct.data.DatabaseProvider
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity
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

    private var _estimatedRegularSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedRegularSalary: State<Int> by lazy { _estimatedRegularSalary }

    private var _estimatedOvertimeSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedOvertimeSalary: State<Int> by lazy { _estimatedOvertimeSalary }

    private var _estimatedLateNightSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedLateNightSalaryState: State<Int> by lazy { _estimatedLateNightSalary }

    // Total worked hours
    private var _totalWorkedHours: MutableState<Int?> = mutableStateOf(null)
    val totalWorkedHours: State<Int?> by lazy { _totalWorkedHours }

    // Total worked days
    private var _totalWorkedDays: MutableState<Int?> = mutableStateOf(null)
    val totalWorkedDays: State<Int?> by lazy { _totalWorkedDays }

    // Total registered days
    private var _totalRegisteredDays: MutableState<Int> = mutableIntStateOf(0)
    val totalRegisteredDays: State<Int> by lazy { _totalRegisteredDays }

    // Total overtime hours
    private var _totalOvertimeHours: MutableState<Int?> = mutableStateOf(null)
    val totalOvertimeHours: State<Int?> by lazy { _totalOvertimeHours }

    // Total regular hours
    private val _totalRegularHours: MutableState<Int?> = mutableStateOf(null)
    val totalRegularHours: State<Int?> by lazy { _totalRegularHours }

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
                // Reset all calculated values
                _estimatedSalary.value = null
                _estimatedRegularSalary.value = 0
                _estimatedOvertimeSalary.value = 0
                _estimatedLateNightSalary.value = 0
                _totalWorkedHours.value = null
                _totalWorkedDays.value = null
                _totalOvertimeHours.value = null
                _totalRegularHours.value = null

                // Calendar events
                val events: List<CalendarEvent> = workdays.map {
                    CalendarEvent(
                        date = it.getLocalDate(),
                        color = it.getWorkdayTypeColor()
                    )
                }

                _calendarState.setEvents(events)

                // Total registered days
                _totalRegisteredDays.value = workdays.size

                // Other calculations
                for (workday in workdays) {
                    // TODO: calculate workdays and set payments based on minutes
                }
            }
        }
    }
}
