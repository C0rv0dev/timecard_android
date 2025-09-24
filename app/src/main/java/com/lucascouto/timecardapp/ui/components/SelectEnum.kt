package com.lucascouto.timecardapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lucascouto.timecardapp.struct.data.enums.WorkdayTypeEnum

@Composable
fun SelectEnum(
    options: List<Pair<String, Int>>,
    onSelect: (Int) -> Unit,
    selected: Int? = null,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .clickable { expanded = true }
    ) {
        OutlinedTextField(
            value = WorkdayTypeEnum.from(selected),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text("Select type") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            for (option in options) {
                DropdownMenuItem(
                    text = { Text(option.first) },
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onSelect(option.second)
                        expanded = false
                    },
                )
            }
        }
    }
}