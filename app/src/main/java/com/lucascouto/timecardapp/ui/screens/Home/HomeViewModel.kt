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
import com.lucascouto.timecardapp.struct.data.singletons.LoadingController
import com.lucascouto.timecardapp.struct.data.storage.DataStorageManager
import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.CalendarState
import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.models.CalendarEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.also

class HomeViewModel(
    private val workdayRepository: WorkdayRepository = DatabaseProvider.workdayRepository,
    val dataStorageManager: DataStorageManager,
) :
    ViewModel() {
    // Calendar
    private val _calendarState = CalendarState()
    val calendarState by lazy { _calendarState }

    // Workdays
    private lateinit var _workdays: List<WorkdayEntity>

    // Estimated Salary
    private var _grossSalary: MutableState<Int> = mutableIntStateOf(0)
    val grossSalary: State<Int?> by lazy { _grossSalary }

    private var _netSalary: MutableState<Int> = mutableIntStateOf(0)
    val netSalary: State<Int?> by lazy { _netSalary }

    private var _estimatedRegularSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedRegularSalary: State<Int> by lazy { _estimatedRegularSalary }

    private var _estimatedOvertimeSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedOvertimeSalary: State<Int> by lazy { _estimatedOvertimeSalary }

    private var _estimatedLateNightSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedLateNightSalaryState: State<Int> by lazy { _estimatedLateNightSalary }

    private var _estimatedBonusSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedBonusSalary: State<Int> by lazy { _estimatedBonusSalary }

    private var _estimatedLocomotionAllowance: MutableState<Int> = mutableIntStateOf(0)
    val estimatedLocomotionAllowance: State<Int> by lazy { _estimatedLocomotionAllowance }

    private var _estimatedPaidAllowances: MutableState<Int> = mutableIntStateOf(0)
    val estimatedPaidAllowances: State<Int> by lazy { _estimatedPaidAllowances }

    // Total worked days
    private var _totalWorkedDays: MutableState<Int?> = mutableStateOf(null)
    val totalWorkedDays: State<Int?> by lazy { _totalWorkedDays }

    // Total worked hours
    private var _totalWorkedHours: MutableState<Int> = mutableStateOf(0)
    val totalWorkedHours: State<Int> by lazy { _totalWorkedHours }

    // Total registered days
    private var _totalRegisteredDays: MutableState<Int> = mutableIntStateOf(0)
    val totalRegisteredDays: State<Int> by lazy { _totalRegisteredDays }

    // Total overtime hours
    private var _totalOvertimeHours: MutableState<Int> = mutableStateOf(0)
    val totalOvertimeHours: State<Int> by lazy { _totalOvertimeHours }

    // Total regular hours
    private val _totalRegularHours: MutableState<Int> = mutableStateOf(0)
    val totalRegularHours: State<Int> by lazy { _totalRegularHours }

    // ==============================================================================================
    // Deductions
    // ==============================================================================================
    private val _unemploymentInsuranceDeduction: MutableState<Int> = mutableIntStateOf(0)
    val unemploymentInsuranceDeduction: State<Int> by lazy { _unemploymentInsuranceDeduction }

    // Init
    init {
        _calendarState.onNextMonth = { refreshData() }
        _calendarState.onPreviousMonth = { refreshData() }
    }

    // Methods
    fun refreshData() {
        LoadingController.setLoading(true)

        viewModelScope.launch {
            _workdays = withContext(Dispatchers.Default) {
                workdayRepository.fetchByMonth(_calendarState.yearMonth())
            }.also { workdays ->
                // Reset all calculated values
                resetCalculatedValues()

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
                _totalWorkedDays.value =
                    workdays.count { it.shiftType == WorkdayTypeEnum.REGULAR.value }

                // Other calculations
                for (workday in workdays) {
                    val workdayData = workday.getWorkdayData()

                    _estimatedBonusSalary.value += workdayData["bonus"] ?: 0
                    _estimatedRegularSalary.value += workdayData["regular"] ?: 0
                    _estimatedOvertimeSalary.value += workdayData["overtime"] ?: 0
                    _estimatedPaidAllowances.value += workdayData["allowances"] ?: 0
                    _estimatedLateNightSalary.value += workdayData["late_night"] ?: 0

                    _totalOvertimeHours.value += (workdayData["overtime_hours"] ?: 0)
                    _totalRegularHours.value += (workdayData["regular_hours"] ?: 0)

                    _totalWorkedHours.value += (workdayData["regular_hours"] ?: 0) +
                            (workdayData["overtime_hours"] ?: 0)

                    _grossSalary.value += (workdayData["bonus"] ?: 0) +
                            (workdayData["regular"] ?: 0) +
                            (workdayData["overtime"] ?: 0) +
                            (workdayData["late_night"] ?: 0) +
                            (workdayData["allowances"] ?: 0)

                    // calc locomotion allowance
                    if (workday.shiftType == WorkdayTypeEnum.REGULAR.value || workday.shiftType == WorkdayTypeEnum.PAID_LEAVE.value) {
                        val locomotionAllowance = dataStorageManager.getSettings().locomotionAllowance
                        _estimatedLocomotionAllowance.value += locomotionAllowance
                        _grossSalary.value += locomotionAllowance
                    }
                }

                // net salary
                _netSalary.value = _grossSalary.value

                // calc deductions
                _unemploymentInsuranceDeduction.value = _grossSalary.value.let {
                    (it * 5.5 / 1000).coerceAtLeast(0.0).toInt()
                }

                applyDeductions()

                LoadingController.setLoading(false)
            }
        }
    }

    private fun resetCalculatedValues() {
        _grossSalary.value = 0
        _netSalary.value = 0
        _estimatedRegularSalary.value = 0
        _estimatedOvertimeSalary.value = 0
        _estimatedLateNightSalary.value = 0
        _estimatedBonusSalary.value = 0
        _estimatedLocomotionAllowance.value = 0
        _estimatedPaidAllowances.value = 0
        _totalWorkedHours.value = 0
        _totalOvertimeHours.value = 0
        _totalRegularHours.value = 0
        _unemploymentInsuranceDeduction.value = 0
    }

    private fun applyDeductions() {
        _netSalary.value = _grossSalary.value -
                _unemploymentInsuranceDeduction.value
    }
}
