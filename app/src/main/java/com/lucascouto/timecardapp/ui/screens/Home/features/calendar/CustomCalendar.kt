package com.lucascouto.timecardapp.ui.screens.Home.features.calendar

import androidx.compose.foundation.background
import com.lucascouto.timecardapp.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.models.CalendarCellData
import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.models.CalendarEvent
import java.time.LocalDate

@Composable
fun CustomCalendar(state: CalendarState) {
    val currentDate = state.currentDate.value
    val cells = state.cells.value

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CalendarHeader(
                currentDate = currentDate,
                onNext = { state.goToNextMonth() },
                onPrevious = { state.goToPreviousMonth() }
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color.LightGray,
                thickness = 1.dp
            )

            CalendarSubHeader()

            CalendarBody(cells = cells)
        }
    }
}

@Composable
fun CalendarHeader(
    currentDate: LocalDate,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ElevatedButton(onClick = onPrevious) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = "Previous Month"
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(currentDate.year.toString())
            Text(currentDate.month.name, style = TextStyle(fontSize = 24.sp))
        }

        ElevatedButton(onClick = onNext) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "Next Month"
            )
        }
    }
}

@Composable
fun CalendarSubHeader() {
    val daysOfWeek = remember { listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat") }

    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(daysOfWeek.size, key = { it }) { index ->
            Text(
                text = daysOfWeek[index],
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun CalendarBody(cells: List<CalendarCellData>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(cells.size, key = { cells[it].hashCode() }) { index ->
            CalendarCell(
                date = cells[index].day,
                isToday = cells[index].isToday,
                faded = cells[index].isFaded,
                event = cells[index].event
            )
        }
    }
}

@Composable
fun CalendarCell(
    date: Int,
    isToday: Boolean,
    faded: Boolean,
    event: CalendarEvent? = null
) {
    val backgroundColor = if (isToday) Color(0xFFBBDEFB) else Color.Transparent
    val textColor = if (faded) Color.Gray
    else if (isToday) Color.Black
    else Color.Unspecified

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = date.toString(), color = textColor, fontSize = 14.sp)

        if (event != null) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(Color(event.color), shape = CircleShape)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
            )
        }
    }
}

//#endregion Components

//#region Preview
@Preview
@Composable
fun CustomCalendarPreview() {
    CustomCalendar(CalendarState())
}
//#endregion Preview