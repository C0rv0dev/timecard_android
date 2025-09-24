package com.lucascouto.timecardapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    label: String,
    displayValue: String?,
    onConfirm: (TimePickerState) -> Unit
) {
    var open by remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()

    // if displayValue is null, use current time, else parse displayValue
    val timePickerState = rememberTimePickerState(
        initialHour = displayValue?.split(":")?.getOrNull(0)?.toIntOrNull()
            ?: currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = displayValue?.split(":")?.getOrNull(1)?.toIntOrNull()
            ?: currentTime.get(Calendar.MINUTE)
    )

    // Open the dialog
    Box(modifier = Modifier.fillMaxWidth().clickable { open = true }) {
        OutlinedTextField(
            value = displayValue ?: "",
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth()
        )
    }


//    display the dialog
    TimePickerDialog(
        open = open,
        onDismiss = { open = false },
        onConfirm = { onConfirm(timePickerState); open = false }
    ) {
        TimePicker(state = timePickerState)
    }
}

@Composable
fun TimePickerDialog(
    open: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    if (!open) return

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}
