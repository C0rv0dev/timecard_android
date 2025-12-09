package com.lucascouto.timecardapp.ui.screens.Home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
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
        Column(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                CustomCalendar(
                    state = viewModel.calendarState,
                    onCellClick = { date ->
                        navController.navigate("${Screens.ShowEntry.route}/${date}")
                    }
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            MonthOverviewGraph(
                totalRegisteredDays = viewModel.totalRegisteredDays.value,
                totalWorkedDays = viewModel.totalWorkedDays.value,
                totalAbsentDays = viewModel.totalAbsentDays.value,
                totalWorkedHours = viewModel.totalWorkedHours.value,
                totalRegularHours = viewModel.totalRegularHours.value,
                totalOvertimeHours = viewModel.totalOvertimeHours.value,
                totalLateNightHours = viewModel.totalLateNightHours.value,
            )

            Spacer(modifier = Modifier.padding(8.dp))

            EstimatedSalaryCard(
                viewModel.netSalary.value,
                viewModel.grossSalary.value,
                viewModel.estimatedBonusSalary.value,
                viewModel.estimatedLocomotionAllowance.value,
                viewModel.estimatedPaidAllowances.value,
                viewModel.estimatedRegularSalary.value,
                viewModel.estimatedOvertimeSalary.value,
                viewModel.estimatedLateNightSalaryState.value,
                // deductions
                viewModel.unemploymentInsuranceDeduction.value,
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(viewModel(), rememberNavController())
}
