package com.lucascouto.timecardapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
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
    buttonStyle: ButtonColors? = null,
) {
    var openDialog by remember { mutableStateOf(false) }

    ElevatedButton(
        onClick = { openDialog = true },
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        colors = buttonStyle ?: ButtonColors(
            containerColor = androidx.compose.material3.ButtonDefaults.elevatedButtonColors().containerColor,
            contentColor = androidx.compose.material3.ButtonDefaults.elevatedButtonColors().contentColor,
            disabledContainerColor = androidx.compose.material3.ButtonDefaults.elevatedButtonColors().disabledContainerColor,
            disabledContentColor = androidx.compose.material3.ButtonDefaults.elevatedButtonColors().disabledContentColor,
        )
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
