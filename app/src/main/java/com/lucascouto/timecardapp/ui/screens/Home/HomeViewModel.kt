package com.lucascouto.timecardapp.ui.screens.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascouto.timecardapp.struct.data.DatabaseProvider
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity
import com.lucascouto.timecardapp.struct.data.repositories.WorkdayRepository
import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.CalendarState
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
    val workdays by lazy { _workdays }

    // Estimated Salary
    private val _estimatedSalary = 123456
    val estimatedSalary by lazy { _estimatedSalary }

    init {
        viewModelScope.launch {
            _workdays = withContext(Dispatchers.Default) {
                workdayRepository.fetch()
            }
        }
    }
}
