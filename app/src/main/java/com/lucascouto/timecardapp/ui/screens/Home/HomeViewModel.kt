package com.lucascouto.timecardapp.ui.screens.Home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascouto.timecardapp.struct.data.DatabaseProvider
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity
import com.lucascouto.timecardapp.struct.data.enums.WorkdayTypeEnum
import com.lucascouto.timecardapp.struct.data.repositories.WorkdayRepository
import com.lucascouto.timecardapp.struct.data.utils.TimeUtils
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
    private var _estimatedSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedSalary: State<Int?> by lazy { _estimatedSalary }

    private var _estimatedRegularSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedRegularSalary: State<Int> by lazy { _estimatedRegularSalary }

    private var _estimatedOvertimeSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedOvertimeSalary: State<Int> by lazy { _estimatedOvertimeSalary }

    private var _estimatedLateNightSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedLateNightSalaryState: State<Int> by lazy { _estimatedLateNightSalary }

    private var _estimatedBonusSalary:  MutableState<Int> = mutableIntStateOf(0)
    val estimatedBonusSalary: State<Int> by lazy { _estimatedBonusSalary }

    private var _estimatedPaidAllowances: MutableState<Int> = mutableIntStateOf(0)
    val estimatedPaidAllowances: State<Int> by lazy { _estimatedPaidAllowances }

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
                _estimatedSalary.value = 0
                _estimatedRegularSalary.value = 0
                _estimatedOvertimeSalary.value = 0
                _estimatedLateNightSalary.value = 0
                _estimatedPaidAllowances.value = 0
                _estimatedBonusSalary.value = 0
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
                _totalWorkedDays.value = workdays.count { it.shiftType == WorkdayTypeEnum.REGULAR.value }

                // Other calculations
                for (workday in workdays) {
                    val salaries = workday.calculateSalary()

                    _estimatedBonusSalary.value += salaries["bonus"] ?: 0
                    _estimatedRegularSalary.value += salaries["regular"] ?: 0
                    _estimatedOvertimeSalary.value += salaries["overtime"] ?: 0
                    _estimatedPaidAllowances.value += salaries["allowances"] ?: 0
                    _estimatedLateNightSalary.value += salaries["late_night"] ?: 0

                    _estimatedSalary.value += (salaries["bonus"] ?: 0) +
                            (salaries["regular"] ?: 0) +
                            (salaries["overtime"] ?: 0) +
                            (salaries["late_night"] ?: 0) +
                            (salaries["allowances"] ?: 0)

                    _totalWorkedHours.value = _totalWorkedHours.value?.plus(TimeUtils.convertTimeToMinutes(workday.shiftDuration) ?: 0)
                }
            }
        }
    }
}
