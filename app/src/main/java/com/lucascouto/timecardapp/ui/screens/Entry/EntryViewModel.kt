package com.lucascouto.timecardapp.ui.screens.Entry

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascouto.timecardapp.struct.data.DatabaseProvider
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity
import com.lucascouto.timecardapp.struct.data.repositories.WorkdayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntryViewModel(private val workdayRepository: WorkdayRepository = DatabaseProvider.workdayRepository) :
    ViewModel() {
    // Vars
    private val _selectedDate: MutableState<String> = mutableStateOf("")
    val selectedDate: State<String> by lazy { _selectedDate }

    val _workday: MutableState<WorkdayEntity?> = mutableStateOf(null)
    val workday: State<WorkdayEntity?> by lazy { _workday }

    // Boot
    fun boot(date: String): EntryViewModel {
        _selectedDate.value = parseDate(date)
        findWorkdayByDate(date)

        return this
    }

    // Methods
    fun setWorkday(workday: WorkdayEntity?) {
        if (workday == null) return

        // Shift parse
        workday.shiftStartHour = parseTime(workday.shiftStartHour)
        workday.shiftEndHour = parseTime(workday.shiftEndHour)
        workday.shiftDuration = calculateDuration(workday.shiftStartHour, workday.shiftEndHour)
        // Lunch parse
        workday.lunchStartHour = parseTime(workday.lunchStartHour)

        _workday.value = workday
    }

    // Workday Methods
    fun saveWorkday(onComplete: () -> Unit) {
        if (_workday.value == null) return

        // Ensure the workday date matches the selected date
        _workday.value = _workday.value!!.copy(date = unparseDate(_selectedDate.value))

        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                workdayRepository.create(workday.value!!)
            }

            onComplete()
        }
    }

    fun editWorkday(onComplete: () -> Unit) {
        if (_workday.value == null || _workday.value!!.id == null) return

        // Ensure the workday date matches the selected date
        _workday.value = _workday.value!!.copy(date = unparseDate(_selectedDate.value))

        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                workdayRepository.update(workday.value!!)
            }

            onComplete()
        }
    }

    fun deleteWorkday(onComplete: () -> Unit) {
        if (_workday.value == null || _workday.value!!.id == null) return

        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                workdayRepository.delete(workday.value!!)
            }

            onComplete()
        }
    }

    fun findWorkdayByDate(date: String, onComplete: () -> Unit = {}) {
        if (date.isEmpty()) return

        viewModelScope.launch {
            val foundWorkday = withContext(Dispatchers.IO) {
                workdayRepository.find(date)
            }

            if (foundWorkday != null) {
                setWorkday(foundWorkday)
            } else {
                setWorkday(WorkdayEntity.default())
            }

            onComplete()
        }
    }

    // Utils
    private fun calculateDuration(start: String, end: String): String {
        val startParts = start.split(":")
        val endParts = end.split(":")

        if (startParts.size != 2 || endParts.size != 2) return ""

        val startHour = startParts[0].toIntOrNull() ?: return ""
        val startMinute = startParts[1].toIntOrNull() ?: return ""
        val endHour = endParts[0].toIntOrNull() ?: return ""
        val endMinute = endParts[1].toIntOrNull() ?: return ""

        var totalStartMinutes = startHour * 60 + startMinute
        var totalEndMinutes = endHour * 60 + endMinute

        if (totalEndMinutes < totalStartMinutes) {
            totalEndMinutes += 24 * 60
        }

        val durationMinutes = totalEndMinutes - totalStartMinutes
        val durationHours = durationMinutes / 60
        val durationRemainingMinutes = durationMinutes % 60

        val hoursString = if (durationHours < 10) "0$durationHours" else "$durationHours"
        val minutesString =
            if (durationRemainingMinutes < 10) "0$durationRemainingMinutes" else "$durationRemainingMinutes"

        return "$hoursString:$minutesString"
    }

    private fun parseTime(time: String): String {
        val parts = time.split(":")

        if (parts.size != 2) return ""

        val hour = parts[0].toIntOrNull() ?: return ""
        val minute = parts[1].toIntOrNull() ?: return ""

        val hourString = if (hour < 10) "0$hour" else "$hour"
        val minuteString = if (minute < 10) "0$minute" else "$minute"

        return "$hourString:$minuteString"
    }

    private fun parseDate(date: String): String {
        val parts = date.split("-")

        if (parts.size != 3) return ""

        val year = parts[0].toIntOrNull() ?: return ""
        val month = parts[1].toIntOrNull() ?: return ""
        val day = parts[2].toIntOrNull() ?: return ""

        val dayString = if (day < 10) "0$day" else "$day"
        val monthString = if (month < 10) "0$month" else "$month"
        val yearString = "$year"

        return "$dayString/$monthString/$yearString"
    }

    private fun unparseDate(date: String): String {
        val parts = date.split("/")

        if (parts.size != 3) return ""

        val day = parts[0].toIntOrNull() ?: return ""
        val month = parts[1].toIntOrNull() ?: return ""
        val year = parts[2].toIntOrNull() ?: return ""

        val dayString = if (day < 10) "0$day" else "$day"
        val monthString = if (month < 10) "0$month" else "$month"
        val yearString = "$year"

        return "$yearString-$monthString-$dayString"
    }
}