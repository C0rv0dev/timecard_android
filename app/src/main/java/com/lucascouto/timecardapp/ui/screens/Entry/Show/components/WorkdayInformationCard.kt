package com.lucascouto.timecardapp.ui.screens.Entry.Show.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lucascouto.timecardapp.R
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity
import com.lucascouto.timecardapp.struct.data.enums.WorkdayTypeEnum
import com.lucascouto.timecardapp.struct.data.utils.TimeUtils
import com.lucascouto.timecardapp.struct.navigation.Screens
import com.lucascouto.timecardapp.ui.components.ActionButton
import com.lucascouto.timecardapp.ui.screens.Entry.EntryViewModel
import java.time.LocalDate

@Composable
fun WorkdayInformationCard(
    viewModel: EntryViewModel,
    navController: NavController,
    date: String,
    workday: WorkdayEntity? = null,
) {
    Card {
        if (workday == null || workday.id == null) {
            Column(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "If you believe this is an error, please contact support.",
                    Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = TextStyle(color = Color(0x80B00020))
                )

                Text(
                    "No workday data found for this date, touch the button below to add one.",
                    Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                )

                Spacer(Modifier.padding(8.dp))

                // green 0xFF4CAF50
                ElevatedButton(
                    onClick = { navController.navigate(Screens.AddEntry.route + "/$date") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.Black,
                        disabledContainerColor = Color(0x804CAF50),
                        disabledContentColor = Color(0x80000000)
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add_entry),
                        contentDescription = "Add"
                    )
                }
            }
        } else {
            Card(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Shift Type")
                    Text(WorkdayTypeEnum.from(workday.shiftType))
                }

                if (workday.shiftType == WorkdayTypeEnum.REGULAR.value) {


                    Row(
                        Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Shift Start Hour")
                        Text(workday.shiftStartHour)
                    }

                    Row(
                        Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Shift End Hour")
                        Text(workday.shiftEndHour)
                    }

                    Row(
                        Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Shift Duration")
                        Text(workday.shiftDuration)
                    }

                    Row(
                        Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Shift Effective Duration")
                        Text(TimeUtils.convertMinutesToTime(TimeUtils.convertTimeToMinutes(workday.shiftDuration)?.minus(workday.lunchDurationMinutes) ?: 0))
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    Row(
                        Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Lunch Start")
                        Text(workday.lunchStartHour)
                    }

                    Row(
                        Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Lunch Duration")
                        Text("${workday.lunchDurationMinutes} minutes")
                    }
                }

                Spacer(Modifier.padding(8.dp))

                // yellow 0xFFFFD60A
                ElevatedButton(
                    onClick = {
                        navController.navigate("${Screens.EditEntry.route}/${workday.date}")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonColors(
                        containerColor = Color(0xFFFFD60A),
                        contentColor = Color.Black,
                        disabledContainerColor = Color(0x80FFD60A),
                        disabledContentColor = Color(0x80000000)
                    )
                ) {
                    Icon(painter = painterResource(R.drawable.ic_edit), contentDescription = "Edit")
                }

                Spacer(Modifier.padding(4.dp))

                ActionButton(
                    buttonContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_trash),
                            contentDescription = "Delete"
                        )
                    },
                    dialogContent = {
                        Text("Are you sure you want to delete this workday entry?")
                    },
                    onConfirm = {
                        viewModel.deleteWorkday { navController.popBackStack() }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun WorkdayInformationCardWorkdayPreview() {
    WorkdayInformationCard(
        viewModel(),
        rememberNavController(),
        LocalDate.now().toString(),
        WorkdayEntity.default(-1)
    )
}

@Preview
@Composable
fun WorkdayInformationCardNullPreview() {
    WorkdayInformationCard(
        viewModel(),
        rememberNavController(),
        LocalDate.now().toString(),
        null
    )
}
