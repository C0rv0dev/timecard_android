package com.lucascouto.timecardapp.ui.screens.Home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascouto.timecardapp.struct.data.DatabaseProvider
import com.lucascouto.timecardapp.struct.data.entities.WorkdayCalcs
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
import kotlin.math.ceil

class HomeViewModel(
    private val workdayRepository: WorkdayRepository = DatabaseProvider.workdayRepository,
    val dataStorageManager: DataStorageManager,
) : ViewModel() {
    // Calendar
    private val _calendarState = CalendarState()
    val calendarState by lazy { _calendarState }

    // Workdays
    private lateinit var _workdays: List<WorkdayEntity>

    // ==============================================================================================
    // Salaries
    // ==============================================================================================
    // Estimated Salary
    private var _grossSalary: MutableState<Int> = mutableIntStateOf(0)
    val grossSalary: State<Int?> by lazy { _grossSalary }

    private var _netSalary: MutableState<Int> = mutableIntStateOf(0)
    val netSalary: State<Int?> by lazy { _netSalary }

    private var _estimatedRegularSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedRegularSalary: State<Int> by lazy { _estimatedRegularSalary }

    private var _estimatedBonusSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedBonusSalary: State<Int> by lazy { _estimatedBonusSalary }

    private var _estimatedOvertimeSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedOvertimeSalary: State<Int> by lazy { _estimatedOvertimeSalary }

    private var _estimatedLateNightSalary: MutableState<Int> = mutableIntStateOf(0)
    val estimatedLateNightSalaryState: State<Int> by lazy { _estimatedLateNightSalary }

    private var _estimatedLocomotionAllowance: MutableState<Int> = mutableIntStateOf(0)
    val estimatedLocomotionAllowance: State<Int> by lazy { _estimatedLocomotionAllowance }

    private var _estimatedPaidAllowances: MutableState<Int> = mutableIntStateOf(0)
    val estimatedPaidAllowances: State<Int> by lazy { _estimatedPaidAllowances }

    // ==============================================================================================
    // Work days statistics
    // ==============================================================================================
    // Total registered days
    private var _totalRegisteredDays: MutableState<Int> = mutableIntStateOf(0)
    val totalRegisteredDays: State<Int> by lazy { _totalRegisteredDays }

    // Total worked days
    private var _totalWorkedDays: MutableState<Int> = mutableIntStateOf(0)
    val totalWorkedDays: State<Int> by lazy { _totalWorkedDays }

    // Total absent days
    private var _totalAbsentDays: MutableState<Int> = mutableIntStateOf(0)
    val totalAbsentDays: State<Int> by lazy { _totalAbsentDays }

    // ==============================================================================================
    // Work hours statistics
    // ==============================================================================================
    // Total worked hours
    private var _totalWorkedHours: MutableState<Float> = mutableFloatStateOf(0f)
    val totalWorkedHours: State<Float> by lazy { _totalWorkedHours }

    // Total regular hours
    private val _totalRegularHours: MutableState<Float> = mutableFloatStateOf(0f)
    val totalRegularHours: State<Float> by lazy { _totalRegularHours }

    // Total overtime hours
    private var _totalOvertimeHours: MutableState<Float> = mutableFloatStateOf(0f)
    val totalOvertimeHours: State<Float> by lazy { _totalOvertimeHours }

    private var _totalLateNightHours: MutableState<Float> = mutableFloatStateOf(0f)
    val totalLateNightHours: State<Float> by lazy { _totalLateNightHours }

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
                        date = it.getLocalDate(), color = it.getWorkdayTypeColor()
                    )
                }

                _calendarState.setEvents(events)

                // Total registered days
                _totalRegisteredDays.value = workdays.size
                _totalWorkedDays.value = workdays.count {
                    it.shiftType == WorkdayTypeEnum.REGULAR.value
                }

                // Other calculations
                for (workday in workdays) {
                    val workdayData = workday.getHours()

                    // Locomotion allowance
                    _estimatedLocomotionAllowance.value += workdayData.calc(
                        type = WorkdayCalcs.LOCOMOTION,
                        rate = dataStorageManager.getSettings().locomotionAllowance
                    )

                    // Is day off
                    if (workdayData.allowance) {
                        _estimatedPaidAllowances.value += workdayData.calc(type = WorkdayCalcs.ALLOWANCE)
                        _totalAbsentDays.value += 1
                        continue
                    }

                    // Set hours
                    _totalWorkedHours.value += (workdayData.regularHours + workdayData.overtimeHours)
                    _totalRegularHours.value += workdayData.regularHours
                    _totalOvertimeHours.value += workdayData.overtimeHours
                    _totalLateNightHours.value += workdayData.lateNightHours

                    // calc salaries
                    _estimatedRegularSalary.value += workdayData.calc(type = WorkdayCalcs.REGULAR)
                    _estimatedBonusSalary.value += workdayData.calc(type = WorkdayCalcs.BONUS)
                    _estimatedOvertimeSalary.value += workdayData.calc(type = WorkdayCalcs.OVERTIME)
                    _estimatedLateNightSalary.value += workdayData.calc(type = WorkdayCalcs.LATE_NIGHT)
                }

                calcNetAndGrossSalaries()

                applyDeductions()

                LoadingController.setLoading(false)
            }
        }
    }

    private fun calcNetAndGrossSalaries() {
        _grossSalary.value =
            _estimatedRegularSalary.value +
                    _estimatedBonusSalary.value +
                    _estimatedOvertimeSalary.value +
                    _estimatedLateNightSalary.value +
                    _estimatedLocomotionAllowance.value +
                    _estimatedPaidAllowances.value

        // Unemployment insurance deduction
        _unemploymentInsuranceDeduction.value = ceil((_grossSalary.value * 5.5f) / 1000).toInt()
    }

    private fun applyDeductions() {
        _netSalary.value =
            _grossSalary.value -
                    _unemploymentInsuranceDeduction.value
    }

    private fun resetCalculatedValues() {
        // Salaries
        _grossSalary.value = 0
        _netSalary.value = 0
        _estimatedRegularSalary.value = 0
        _estimatedOvertimeSalary.value = 0
        _estimatedLateNightSalary.value = 0
        _estimatedBonusSalary.value = 0
        _estimatedLocomotionAllowance.value = 0
        _estimatedPaidAllowances.value = 0
        // Work days
        _totalWorkedDays.value = 0
        _totalAbsentDays.value = 0
        // Work hours
        _totalWorkedHours.value = 0f
        _totalRegularHours.value = 0f
        _totalOvertimeHours.value = 0f
        _totalLateNightHours.value = 0f
        // Deductions
        _unemploymentInsuranceDeduction.value = 0
    }
}
