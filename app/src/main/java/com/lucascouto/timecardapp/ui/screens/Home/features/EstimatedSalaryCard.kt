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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EstimatedSalaryCard(
    estimatedSalary: Int? = 123456,
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

            Text(
                "¥${formattedSalary}",
                style = TextStyle(fontSize = 42.sp, color = Color(0xFF2D7332))
            )

            Spacer(Modifier.padding(4.dp))

            Text(
                "Regular payment: ¥${String.format("%,d", estimatedRegularSalary).replace(',', '.')}",
                style = TextStyle(
                    color = Color(0xFF555555),
                    fontSize = 12.sp,
                )
            )

            Spacer(Modifier.padding(4.dp))

            Text(
                "Overtime payment: ¥${String.format("%,d", estimatedOvertimeSalary).replace(',', '.')}",
                style = TextStyle(
                    color = Color(0xFF555555),
                    fontSize = 12.sp,
                )
            )

            Spacer(Modifier.padding(4.dp))

            Text(
                "Late night payment: ¥${String.format("%,d", estimatedLateNightSalary).replace(',', '.')}",
                style = TextStyle(
                    color = Color(0xFF555555),
                    fontSize = 12.sp,
                )
            )
        }
    }
}

@Preview
@Composable
fun EstimatedSalaryCardPreview() {
    EstimatedSalaryCard()
}
