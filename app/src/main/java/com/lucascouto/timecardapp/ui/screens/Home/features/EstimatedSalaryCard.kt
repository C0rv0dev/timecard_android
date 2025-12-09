package com.lucascouto.timecardapp.ui.screens.Home.features

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucascouto.timecardapp.ui.components.HorizontalDividerWithLabel
import com.lucascouto.timecardapp.ui.components.InfoRow

@Composable
fun EstimatedSalaryCard(
    estimatedNetSalary: Int? = 123456,
    estimatedGrossSalary: Int? = 123456,
    estimatedBonusSalary: Int = 20000,
    estimatedLocomotionBonus: Int = 1500,
    estimatedPaidAllowances: Int = 5000,
    estimatedRegularSalary: Int = 100000,
    estimatedOvertimeSalary: Int = 23000,
    estimatedLateNightSalary: Int = 456,
    // deductions
    estimatedUnemploymentInsurance: Int = 0,
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Estimated Salary", style = TextStyle(fontSize = 20.sp))

            Spacer(Modifier.padding(4.dp))

            HorizontalDividerWithLabel("Earnings")

            InfoRow(
                label = "Regular payment",
                value = estimatedRegularSalary.toFloat(),
                isDayFormat = false,
                isHourFormat = false,
                isMoneyFormat = true,
                isEarning = true,
                isDeduction = false,
                highlight = false
            )

            Spacer(Modifier.padding(4.dp))

            InfoRow(
                label = "Bonus payment",
                value = estimatedBonusSalary.toFloat(),
                isDayFormat = false,
                isHourFormat = false,
                isMoneyFormat = true,
                isEarning = true,
                isDeduction = false,
                highlight = false
            )

            Spacer(Modifier.padding(4.dp))

            InfoRow(
                label = "Overtime payment",
                value = estimatedOvertimeSalary.toFloat(),
                isDayFormat = false,
                isHourFormat = false,
                isMoneyFormat = true,
                isEarning = true,
                isDeduction = false,
                highlight = false
            )

            Spacer(Modifier.padding(4.dp))

            InfoRow(
                label = "Locomotion bonus",
                value = estimatedLocomotionBonus.toFloat(),
                isDayFormat = false,
                isHourFormat = false,
                isMoneyFormat = true,
                isEarning = true,
                isDeduction = false,
                highlight = false
            )

            Spacer(Modifier.padding(4.dp))

            InfoRow(
                label = "Late night payment",
                value = estimatedLateNightSalary.toFloat(),
                isDayFormat = false,
                isHourFormat = false,
                isMoneyFormat = true,
                isEarning = true,
                isDeduction = false,
                highlight = false
            )

            Spacer(Modifier.padding(4.dp))

            InfoRow(
                label = "Paid allowances",
                value = estimatedPaidAllowances.toFloat(),
                isDayFormat = false,
                isHourFormat = false,
                isMoneyFormat = true,
                isEarning = true,
                isDeduction = false,
                highlight = false
            )

            HorizontalDividerWithLabel("Deductions")

            InfoRow(
                label = "Unemployment insurance",
                value = estimatedUnemploymentInsurance.toFloat(),
                isDayFormat = false,
                isHourFormat = false,
                isMoneyFormat = true,
                isEarning = false,
                isDeduction = true,
                highlight = false
            )

            HorizontalDividerWithLabel("Total")

            InfoRow(
                label = "Estimated Gross Salary",
                value = estimatedGrossSalary?.toFloat(),
                isDayFormat = false,
                isHourFormat = false,
                isMoneyFormat = true,
                isEarning = true,
                isDeduction = false,
                highlight = true
            )

            Spacer(Modifier.padding(4.dp))

            InfoRow(
                label = "Estimated Net Salary",
                value = estimatedNetSalary?.toFloat(),
                isDayFormat = false,
                isHourFormat = false,
                isMoneyFormat = true,
                isEarning = true,
                isDeduction = false,
                highlight = true
            )
        }
    }
}

@Preview
@Composable
fun EstimatedSalaryCardPreview() {
    EstimatedSalaryCard()
}
