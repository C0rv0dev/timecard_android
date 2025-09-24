package com.lucascouto.timecardapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun ActionButton(
    content: @Composable () -> Unit,
    onConfirm: () -> Unit,
    enabled: Boolean = true,
) {
    var openDialog by remember { mutableStateOf(false) }

    ElevatedButton(
        onClick = { openDialog = true },
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
    ) {
        content()
    }

    if (openDialog) {
        AlertDialog(
            title = { Text(text = "Confirm Action") },
            text = { Text("Are you sure you want to proceed?") },
            onDismissRequest = { openDialog = false },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        onConfirm()
                        openDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                ElevatedButton(
                    onClick = { openDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
