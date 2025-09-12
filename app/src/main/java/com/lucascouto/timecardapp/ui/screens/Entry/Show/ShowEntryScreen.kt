package com.lucascouto.timecardapp.ui.screens.Entry.Show

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lucascouto.timecardapp.ui.layout.BasePage
import com.lucascouto.timecardapp.ui.screens.Entry.Show.components.WorkdayInformationCard
import com.lucascouto.timecardapp.ui.screens.Entry.EntryViewModel
import java.time.LocalDate

@Composable
fun ShowEntryScreen(
    viewModel: EntryViewModel,
    navController: NavController,
    date: String
) {
    LaunchedEffect(Unit) {
        viewModel.setSelectedDate(date)
    }

    BasePage(navController) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (viewModel.workday.value != null) "Workday ID: ${viewModel.workday.value?.id}" else "Add New Entry",
                        textAlign = TextAlign.Center
                    )

                    HorizontalDivider(modifier = Modifier.padding(4.dp))

                    Text("Selected Date", textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.padding(4.dp))

                    Text(
                        viewModel.selectedDate.value,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 24.sp
                        )
                    )
                }
            }

            Spacer(Modifier.padding(8.dp))

            WorkdayInformationCard(navController, date, viewModel.workday.value)
        }
    }
}

@Preview
@Composable
fun ShowEntryScreenPreview() {
    ShowEntryScreen(
        viewModel(),
        rememberNavController(),
        LocalDate.now().minusDays(1).toString()
    )
}