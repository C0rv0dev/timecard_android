package com.lucascouto.timecardapp.ui.screens

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
import com.lucascouto.timecardapp.ui.layout.BasePage

@Composable
fun HomeScreen() {
    BasePage {
        Column {
        Card(modifier = Modifier.padding(8.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Estimated Salary", style = TextStyle(fontSize = 16.sp))
                Spacer(Modifier.padding(4.dp))
                Text("¥202.000", style = TextStyle(fontSize = 32.sp, color = Color(0xFF2D7332)))
            }
        }

//        Calendar
            Card(modifier = Modifier.padding(8.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Calendar", style = TextStyle(fontSize = 16.sp))
                    Spacer(Modifier.padding(4.dp))
                    Text(
                        "Coming Soon!",
                        style = TextStyle(fontSize = 24.sp, color = Color(0xFF555555))
                    )
                }
            }

//        Graph
            Card(modifier = Modifier.padding(8.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Graph", style = TextStyle(fontSize = 16.sp))
                    Spacer(Modifier.padding(4.dp))
                    Text(
                        "Coming Soon!",
                        style = TextStyle(fontSize = 24.sp, color = Color(0xFF555555))
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
