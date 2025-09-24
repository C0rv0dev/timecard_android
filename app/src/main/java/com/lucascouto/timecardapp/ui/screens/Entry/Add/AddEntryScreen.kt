package com.lucascouto.timecardapp.ui.screens.Entry.Add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lucascouto.timecardapp.R
import com.lucascouto.timecardapp.struct.data.enums.WorkdayTypeEnum
import com.lucascouto.timecardapp.ui.components.SelectEnum
import com.lucascouto.timecardapp.ui.components.TimePickerDialog
import com.lucascouto.timecardapp.ui.layout.BasePage
import com.lucascouto.timecardapp.ui.screens.Entry.EntryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    viewModel: EntryViewModel,
    navController: NavController,
    isEditing: Boolean = false,
) {
    val scrollState = rememberScrollState()

    BasePage(navController) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .verticalScroll(scrollState)
        ) {
            Card(Modifier.fillMaxWidth()) {
                Text(
                    viewModel.selectedDate.value,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    style = TextStyle(fontSize = 24.sp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.padding(4.dp))

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    SelectEnum(
                        options = WorkdayTypeEnum.list(),
                        selected = viewModel.workday.value?.shiftType
                            ?: WorkdayTypeEnum.REGULAR.value,
                        onSelect = { type ->
                            viewModel.setWorkday(
                                viewModel.workday.value?.copy(shiftType = type)
                            )
                        },
                    )

                    if (viewModel.workday.value?.shiftType == WorkdayTypeEnum.REGULAR.value) {
                        Spacer(Modifier.padding(4.dp))

                        TimePickerDialog(
                            label = "Shift Start",
                            displayValue = viewModel.workday.value?.shiftStartHour,
                            onConfirm = { state ->
                                viewModel.setWorkday(
                                    viewModel.workday.value?.copy(shiftStartHour = "${state.hour}:${state.minute}")
                                )
                            },
                        )

                        Spacer(Modifier.padding(4.dp))

                        TimePickerDialog(
                            label = "Shift End",
                            displayValue = viewModel.workday.value?.shiftEndHour,
                            onConfirm = { state ->
                                viewModel.setWorkday(
                                    viewModel.workday.value?.copy(shiftEndHour = "${state.hour}:${state.minute}")
                                )
                            },
                        )

                        HorizontalDivider(Modifier.padding(vertical = 12.dp))

                        TimePickerDialog(
                            label = "Lunch Start",
                            displayValue = viewModel.workday.value?.lunchStartHour,
                            onConfirm = { state ->
                                viewModel.setWorkday(
                                    viewModel.workday.value?.copy(lunchStartHour = "${state.hour}:${state.minute}")
                                )
                            },
                        )

                        Spacer(Modifier.padding(4.dp))

                        OutlinedTextField(
                            label = { Text("Lunch Duration (minutes)") },
                            value = if (viewModel.workday.value?.lunchDurationMinutes == 0) "" else viewModel.workday.value?.lunchDurationMinutes.toString(),
                            onValueChange = {
                                val minutes = it.toIntOrNull() ?: 0
                                viewModel.setWorkday(
                                    viewModel.workday.value?.copy(
                                        lunchDurationMinutes = minutes
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        HorizontalDivider(Modifier.padding(vertical = 12.dp))

                        Row(
                            Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Duration")
                            Text(viewModel.workday.value?.shiftDuration.toString())
                        }
                    }
                }
            }

            Spacer(Modifier.padding(8.dp))

            ElevatedButton(
                onClick = {
                    if (isEditing) viewModel.editWorkday { navController.popBackStack() }
                    else viewModel.saveWorkday { navController.popBackStack() }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.Black,
                    disabledContainerColor = Color(0x804CAF50),
                    disabledContentColor = Color(0x80000000)
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = "Add",

                )
            }

            Spacer(Modifier.padding(4.dp))

            ElevatedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    containerColor = Color(0xFFB00020),
                    contentColor = Color.Black,
                    disabledContainerColor = Color(0x80B00020),
                    disabledContentColor = Color(0x80000000)
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_cancel),
                    contentDescription = "Delete",
                )
            }
        }
    }
}

@Preview
@Composable
fun AddEntryScreenPreview() {
    AddEntryScreen(viewModel(), rememberNavController())
}
