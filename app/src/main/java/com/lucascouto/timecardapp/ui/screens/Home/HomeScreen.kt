package com.lucascouto.timecardapp.ui.screens.Home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lucascouto.timecardapp.ui.layout.BasePage
import com.lucascouto.timecardapp.ui.screens.Home.features.EstimatedSalaryCard
import com.lucascouto.timecardapp.ui.screens.Home.features.MonthOverviewGraph
import com.lucascouto.timecardapp.ui.screens.Home.features.calendar.CustomCalendar

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    BasePage(navController) {
        Column(modifier = Modifier.padding(8.dp)) {
//        Estimated Salary
            EstimatedSalaryCard()

            Spacer(modifier = Modifier.padding(8.dp))

//        Graph
            MonthOverviewGraph()

            Spacer(modifier = Modifier.padding(8.dp))

//        Calendar
            CustomCalendar(viewModel.calendarState)
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(HomeViewModel(), rememberNavController())
}
