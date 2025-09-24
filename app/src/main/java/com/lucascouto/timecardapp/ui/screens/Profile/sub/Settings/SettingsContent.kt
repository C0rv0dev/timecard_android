package com.lucascouto.timecardapp.ui.screens.Profile.sub.Settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lucascouto.timecardapp.ui.components.ActionButton
import com.lucascouto.timecardapp.ui.components.TimePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(viewModel: SettingsViewModel) {
    // States
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Default Values")
        Spacer(Modifier.padding(8.dp))

        OutlinedTextField(
            label = { Text("Hourly Pay") },
            value = viewModel.defaultHourlyPay.value.toString(),
            onValueChange = { viewModel.updateHourlyPay(it) },
            readOnly = !viewModel.isEditing.value,
            enabled = viewModel.isEditing.value,
            modifier = Modifier.fillMaxWidth(),
            // number keyboard
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(4.dp))

        OutlinedTextField(
            label = { Text("Overtime Rate (%)") },
            value = viewModel.defaultOvertimeRate.value.toString(),
            onValueChange = { viewModel.updateOvertimeRate(it) },
            readOnly = !viewModel.isEditing.value,
            enabled = viewModel.isEditing.value,
            modifier = Modifier.fillMaxWidth(),
            // number keyboard
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(4.dp))

        OutlinedTextField(
            label = { Text("Late Night Rate (%)") },
            value = viewModel.lateNightRate.value.toString(),
            onValueChange = { viewModel.updateLateNightRate(it) },
            readOnly = !viewModel.isEditing.value,
            enabled = viewModel.isEditing.value,
            modifier = Modifier.fillMaxWidth(),
            // number keyboard
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(8.dp))
        Text("Default Shift Times")
        Spacer(Modifier.padding(8.dp))

        OutlinedTextField(
            label = { Text("Base Shift Duration (hours)") },
            value = viewModel.baseShiftDurationHours.value.toString(),
            onValueChange = { viewModel.updateBaseShiftDuration(it) },
            readOnly = !viewModel.isEditing.value,
            enabled = viewModel.isEditing.value,
            modifier = Modifier.fillMaxWidth(),
            // number keyboard
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(4.dp))

        TimePickerDialog(
            label = "Shift Start Time",
            displayValue = viewModel.shiftStartTime.value,
            onConfirm = { state -> viewModel.updateShiftStartTime(state) },
            enabled = viewModel.isEditing.value,
        )

        Spacer(Modifier.padding(4.dp))

        TimePickerDialog(
            label = "Shift End Time",
            displayValue = viewModel.shiftEndTime.value,
            onConfirm = { state -> viewModel.updateShiftEndTime(state) },
            enabled = viewModel.isEditing.value,
        )

        Spacer(Modifier.padding(4.dp))

        TimePickerDialog(
            label = "Late Night Start Time",
            displayValue = viewModel.lateNightStartTime.value,
            onConfirm = { state -> viewModel.updateLateNightStartTime(state) },
            enabled = viewModel.isEditing.value,
        )

        Spacer(Modifier.padding(4.dp))

        TimePickerDialog(
            label = "Late Night End Time",
            displayValue = viewModel.lateNightEndTime.value,
            onConfirm = { state -> viewModel.updateLateNightEndTime(state) },
            enabled = viewModel.isEditing.value,
        )

        Spacer(Modifier.padding(8.dp))
        Text("Lunch")
        Spacer(Modifier.padding(8.dp))

        TimePickerDialog(
            label = "Lunch Start Time",
            displayValue = viewModel.lunchStartTime.value,
            onConfirm = { state -> viewModel.updateLunchStartTime(state) },
            enabled = viewModel.isEditing.value,
        )

        Spacer(Modifier.padding(4.dp))

        OutlinedTextField(
            label = { Text("Lunch Duration (minutes)") },
            value = viewModel.lunchDurationMinutes.value.toString(),
            onValueChange = { viewModel.updateLunchDuration(it) },
            readOnly = !viewModel.isEditing.value,
            enabled = viewModel.isEditing.value,
            modifier = Modifier.fillMaxWidth(),
            // number keyboard
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(8.dp))

        // edit
        ElevatedButton(
            onClick = {
                if (viewModel.isEditing.value) {
                    viewModel.saveSettings()
                    viewModel.isEditing.value = false
                } else {
                    viewModel.isEditing.value = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (viewModel.isEditing.value) "Save Settings" else "Edit Settings")
        }

        Spacer(Modifier.padding(4.dp))

        // reset defaults

        ActionButton(
            buttonContent = { Text("Reset to Defaults") },
            dialogContent = { Text("Are you sure you want to reset all settings to their default values? This action cannot be undone.") },
            onConfirm = { TODO() },
        )
    }
}
