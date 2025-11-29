package com.lucascouto.timecardapp.ui.screens.Entry

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascouto.timecardapp.struct.data.DatabaseProvider
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity
import com.lucascouto.timecardapp.struct.data.repositories.WorkdayRepository
import com.lucascouto.timecardapp.struct.data.storage.DataStorageManager
import com.lucascouto.timecardapp.struct.data.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntryViewModel(
    private val workdayRepository: WorkdayRepository = DatabaseProvider.workdayRepository,
    private val dataStoreManager: DataStorageManager
) :
    ViewModel() {
    // Vars
    private val _selectedDate: MutableState<String> = mutableStateOf("")
    val selectedDate: State<String> by lazy { _selectedDate }

    val _workday: MutableState<WorkdayEntity?> = mutableStateOf(null)
    val workday: State<WorkdayEntity?> by lazy { _workday }

    // Boot
    fun boot(date: String): EntryViewModel {
        _selectedDate.value = TimeUtils.parseDate(date)
        findWorkdayByDate(date)

        return this
    }

    // Methods
    fun setWorkday(workday: WorkdayEntity?) {
        if (workday == null) return

        // Shift parse
        workday.shiftStartHour = TimeUtils.parseTime(workday.shiftStartHour)
        workday.shiftEndHour = TimeUtils.parseTime(workday.shiftEndHour)
        workday.shiftDuration = TimeUtils.calculateDuration(
            workday.shiftStartHour,
            workday.shiftEndHour,
            workday.lunchDurationMinutes,
        )
        // Lunch parse
        workday.lunchStartHour = TimeUtils.parseTime(workday.lunchStartHour)

        _workday.value = workday
    }

    // Workday Methods
    fun saveWorkday(onComplete: () -> Unit) {
        if (_workday.value == null) return

        // Ensure the workday date matches the selected date
        _workday.value = _workday.value!!.copy(date = TimeUtils.unparseDate(_selectedDate.value))

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
        _workday.value = _workday.value!!.copy(date = TimeUtils.unparseDate(_selectedDate.value))

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
                val localSettings = dataStoreManager.getSettings()
                println("Loaded Local Settings: $localSettings")
                setWorkday(WorkdayEntity.default(localSettings = localSettings))
            }

            onComplete()
        }
    }
}