package com.lucascouto.timecardapp.ui.screens.Home.features.calendar

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.models.CalendarCellData
import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.models.CalendarEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@Stable
class CalendarState(initialDate: LocalDate = LocalDate.now()) {
    // Coroutine
    private val scope = CoroutineScope(Dispatchers.Default)

    // Variables
    private val _currentDate: MutableState<LocalDate> = mutableStateOf(initialDate)
    val currentDate: State<LocalDate> by lazy { _currentDate }

    private val _events = mutableStateOf<List<CalendarEvent>?>(null)
    private val _cells: MutableState<List<CalendarCellData>> =
        mutableStateOf(generateCalendarCells(initialDate))
    val cells: State<List<CalendarCellData>> by lazy { _cells }

    // Initializer
    init {
        scope.launch {
            withContext(Dispatchers.Main) {
                updateMonth(_currentDate.value)
            }
        }
    }

    // Callbacks
    var onNextMonth: (() -> Unit)? = null
    var onPreviousMonth: (() -> Unit)? = null

    // Methods
    fun goToNextMonth() {
        updateMonth(_currentDate.value.plusMonths(1))
        onNextMonth?.invoke()
    }
    fun goToPreviousMonth() {
        updateMonth(_currentDate.value.minusMonths(1))
        onPreviousMonth?.invoke()
    }

    fun setEvents(events: List<CalendarEvent>) {
        _events.value = events
        updateMonth(_currentDate.value)
    }

    fun yearMonth(): String {
        return "${_currentDate.value.year}-${_currentDate.value.monthValue.toString().padStart(2, '0')}"
    }

    private fun updateMonth(newDate: LocalDate) {
        _currentDate.value = newDate
        scope.launch {
            val newCells = generateCalendarCells(newDate)

            withContext(Dispatchers.Main) {
                _cells.value = newCells
            }
        }
    }

    private fun generateCalendarCells(currentDate: LocalDate): List<CalendarCellData> {
        val daysInMonth = currentDate.lengthOfMonth()
        val firstDayOfWeek = currentDate.withDayOfMonth(1).dayOfWeek.value % 7
        val totalCells = ((daysInMonth + firstDayOfWeek + 6) / 7) * 7

        val prevMonth = currentDate.minusMonths(1)
        val prevMonthDays = prevMonth.lengthOfMonth()
        val today = LocalDate.now()

        return List(totalCells) { index ->
            when {
                index < firstDayOfWeek -> CalendarCellData(
                    date = LocalDate.of(
                        prevMonth.year,
                        prevMonth.month,
                        prevMonthDays - (firstDayOfWeek - index - 1)
                    ),
                    isToday = false,
                    isFaded = true,
                    event = null
                )

                index >= firstDayOfWeek + daysInMonth -> CalendarCellData(
                    date = LocalDate.of(
                        currentDate.year,
                        currentDate.month,
                        1
                    )
                        .plusMonths(1)
                        .withDayOfMonth(index - firstDayOfWeek - daysInMonth + 1),
                    isToday = false,
                    isFaded = true,
                    event = null
                )

                else -> {
                    val day = index - firstDayOfWeek + 1
                    CalendarCellData(
                        date = currentDate.withDayOfMonth(day),
                        isToday = today.dayOfMonth == day && today.month == currentDate.month && today.year == currentDate.year,
                        isFaded = false,
                        event = _events.value?.find { it.date.dayOfMonth == day && it.date.month == currentDate.month && it.date.year == currentDate.year }
                    )
                }
            }
        }
    }
}
