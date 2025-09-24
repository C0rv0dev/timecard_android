package com.lucascouto.timecardapp.ui.screens.Home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lucascouto.timecardapp.struct.navigation.Screens
import com.lucascouto.timecardapp.ui.layout.BasePage
import com.lucascouto.timecardapp.ui.screens.Home.features.EstimatedSalaryCard
import com.lucascouto.timecardapp.ui.screens.Home.features.MonthOverviewGraph
import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.CustomCalendar

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) { viewModel.refreshData() }

    BasePage(navController) {
        Column(modifier = Modifier.padding(8.dp)) {
//        Estimated Salary
            EstimatedSalaryCard(viewModel.estimatedSalary.value)

            Spacer(modifier = Modifier.padding(8.dp))

//        Graph
            MonthOverviewGraph(
                totalRegisteredDays = viewModel.totalRegisteredDays.value,
                totalWorkedDays = viewModel.totalWorkedDays.value,
                totalWorkedHours = viewModel.totalWorkedHours.value,
                totalOvertimeHours = viewModel.totalOvertimeHours.value
            )

            Spacer(modifier = Modifier.padding(8.dp))

//        Calendar
            CustomCalendar(
                viewModel.calendarState,
                { date ->
                    navController.navigate("${Screens.ShowEntry.route}/${date}")
                }
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(viewModel(), rememberNavController())
}
