package com.lucascouto.timecardapp.ui.screens.Home.features

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
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
import com.lucascouto.timecardapp.sys.debug.DebugComposable

@Composable
fun MonthOverviewGraph(totalWorkedHours: Int = 120, totalOvertimeHours: Int = 20) {
    val gapSize = 3

    Card {
        DebugComposable("MonthOverviewGraph")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text("Total Worked Hours", style = TextStyle(fontSize = 16.sp))

                Row {
                    for (i in 0 until gapSize) {
                        Text("-", style = TextStyle(fontSize = 16.sp, color = Color(0xFF555555)))
                    }
                }

                Text(
                    "${totalWorkedHours}h",
                    style = TextStyle(fontSize = 16.sp, color = Color(0xFF2D7332))
                )
            }

            Spacer(Modifier.padding(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text("Total Overtime Hours", style = TextStyle(fontSize = 16.sp))

                Row {
                    for (i in 0 until gapSize) {
                        Text("-", style = TextStyle(fontSize = 16.sp, color = Color(0xFF555555)))
                    }
                }

                Text(
                    "${totalOvertimeHours}h",
                    style = TextStyle(fontSize = 16.sp, color = Color(0xFF2D7332))
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                color = DividerDefaults.color,
                thickness = DividerDefaults.Thickness,
            )

            // pie chart based on workday type
            Text(
                "Pie Chart coming Soon!",
                style = TextStyle(fontSize = 24.sp, color = Color(0xFF555555))
            )
        }
    }
}

@Preview
@Composable
fun MonthOverviewGraphPreview() {
    MonthOverviewGraph()
}
