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
    buttonContent: @Composable () -> Unit,
    dialogContent: @Composable () -> Unit,
    onConfirm: () -> Unit,
    enabled: Boolean = true,
) {
    var openDialog by remember { mutableStateOf(false) }

    ElevatedButton(
        onClick = { openDialog = true },
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
    ) {
        buttonContent()
    }

    if (openDialog) {
        AlertDialog(
            title = { Text(text = "Confirm Action") },
            text = { dialogContent() },
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
