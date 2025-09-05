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

    private val _events = mutableStateOf<List<CalendarEvent>>(generateCalendarEvents(initialDate))
    private val _cells: MutableState<List<CalendarCellData>> =
        mutableStateOf(generateCalendarCells(initialDate))
    val cells: State<List<CalendarCellData>> by lazy { _cells }

    // Initializer
    init {
        scope.launch {
            _events.value = generateCalendarEvents(LocalDate.now())
            withContext(Dispatchers.Main) {
                updateMonth(_currentDate.value)
            }
        }
    }

    // Methods
    fun goToNextMonth() = updateMonth(_currentDate.value.plusMonths(1))
    fun goToPreviousMonth() = updateMonth(_currentDate.value.minusMonths(1))

    private fun updateMonth(newDate: LocalDate) {
        _currentDate.value = newDate
        scope.launch {
            val newCells = generateCalendarCells(newDate)

            withContext(Dispatchers.Main) {
                _cells.value = newCells
            }
        }
    }

    private fun generateCalendarEvents(currentDate: LocalDate): List<CalendarEvent> {
        // generate one event for each odd day of the month
        val daysInMonth = currentDate.lengthOfMonth()
        return (1..daysInMonth).filter { it % 2 == 0 }.map { day ->
            CalendarEvent(
                date = currentDate.withDayOfMonth(day),
                // pick a random color
                color = listOf(0xFFE57373, 0xFF81C784, 0xFF64B5F6).random().toInt()
            )
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
                        event = _events.value.find { it.date.dayOfMonth == day && it.date.month == currentDate.month && it.date.year == currentDate.year }
                    )
                }
            }
        }
    }
}
