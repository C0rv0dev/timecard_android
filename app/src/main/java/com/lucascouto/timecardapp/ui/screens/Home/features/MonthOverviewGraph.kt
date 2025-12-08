package com.lucascouto.timecardapp.ui.screens.Home.features

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucascouto.timecardapp.ui.components.HorizontalDividerWithLabel
import com.lucascouto.timecardapp.ui.components.InfoRow

@Composable
fun MonthOverviewGraph(
    totalRegisteredDays: Int? = 22,
    totalWorkedDays: Int? = 20,
    totalWorkedHours: Int? = 120,
    totalRegularHours: Int? = 100,
    totalOvertimeHours: Int? = 20
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            InfoRow(
                label = "Total registered days",
                value = totalRegisteredDays,
                isDayFormat = true,
                isHourFormat = false,
                isMoneyFormat = false,
                isEarning = false,
                isDeduction = false,
                highlight = false
            )

            Spacer(Modifier.padding(4.dp))

            InfoRow(
                label = "Total worked days",
                value = totalWorkedDays,
                isDayFormat = true,
                isHourFormat = false,
                isMoneyFormat = false,
                isEarning = false,
                isDeduction = false,
                highlight = false
            )

            Spacer(Modifier.padding(4.dp))

            HorizontalDividerWithLabel("Worked hours")

            InfoRow(
                label = "Total Worked Hours",
                value = totalWorkedHours,
                isDayFormat = false,
                isHourFormat = true,
                isMoneyFormat = false,
                isEarning = false,
                isDeduction = false,
                highlight = false
            )

            InfoRow(
                label = "- Regular Hours",
                value = totalRegularHours,
                isDayFormat = false,
                isHourFormat = true,
                isMoneyFormat = false,
                isEarning = false,
                isDeduction = false,
                highlight = false
            )

            InfoRow(
                label = "- Overtime Hours",
                value = totalOvertimeHours,
                isDayFormat = false,
                isHourFormat = true,
                isMoneyFormat = false,
                isEarning = false,
                isDeduction = false,
                highlight = false
            )

            // TODO: Add pie chart
        }
    }
}

@Preview
@Composable
fun MonthOverviewGraphPreview() {
    MonthOverviewGraph()
}
