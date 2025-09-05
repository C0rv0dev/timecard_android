package com.lucascouto.timecardapp.ui.screens.Entry.Show

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
import java.time.LocalDate

class ShowEntryViewModel(private val workdayRepository: WorkdayRepository = DatabaseProvider.workdayRepository) :
    ViewModel() {
    // Vars
    private val _selectedDate: MutableState<String> = mutableStateOf("")
    val selectedDate: State<String> by lazy { _selectedDate }

    val _workday: MutableState<WorkdayEntity?> = mutableStateOf(null)
    val workday: State<WorkdayEntity?> by lazy { _workday }

    // Init
    init {
        _selectedDate.value = parseDate(LocalDate.now().toString())

        viewModelScope.launch {
            _workday.value = withContext(Dispatchers.Default) {
                workdayRepository.find(_selectedDate.value)
            }
        }
    }

    // Methods
    fun setSelectedDate(date: String) {
        _selectedDate.value = parseDate(date)
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
}
