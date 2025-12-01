package com.lucascouto.timecardapp.ui.screens.Home.features

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EstimatedSalaryCard(
    estimatedSalary: Int? = 123456,
    estimatedBonusSalary: Int = 20000,
    estimatedPaidAllowances: Int = 5000,
    estimatedRegularSalary: Int = 100000,
    estimatedOvertimeSalary: Int = 23000,
    estimatedLateNightSalary: Int = 456,
) {
    val formattedSalary = String.format("%,d", estimatedSalary).replace(',', '.')

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Estimated Salary", style = TextStyle(fontSize = 16.sp))

            Spacer(Modifier.padding(4.dp))

            InfoRow("Regular payment", estimatedRegularSalary)

            Spacer(Modifier.padding(4.dp))

            InfoRow("Bonus payment", estimatedBonusSalary)

            Spacer(Modifier.padding(4.dp))

            InfoRow("Overtime payment", estimatedOvertimeSalary)

            Spacer(Modifier.padding(4.dp))

            // TODO: remove comment when late night works
//            InfoRow("Late night payment", estimatedLateNightSalary)
//
//            Spacer(Modifier.padding(4.dp))

            InfoRow("Paid allowances", estimatedPaidAllowances)

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Total", style = TextStyle(fontSize = 16.sp))

            Text(
                "¥${formattedSalary}",
                style = TextStyle(fontSize = 42.sp, color = Color(0xFF2D7332))
            )
        }
    }
}

@Composable
private fun InfoRow(title: String, value: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "${title}:",
            style = TextStyle(
                color = Color(0xFF555555),
                fontSize = 12.sp,
            )
        )

        Text(
            "¥${String.format("%,d", value).replace(',', '.')}",
            style = TextStyle(
                color = Color(0xFF555555),
                fontSize = 12.sp,
            )
        )
    }
}

@Preview
@Composable
fun EstimatedSalaryCardPreview() {
    EstimatedSalaryCard()
}
