package com.lucascouto.timecardapp.ui.screens.Entry.Add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lucascouto.timecardapp.ui.layout.BasePage
import com.lucascouto.timecardapp.ui.screens.Entry.EntryViewModel
import java.time.LocalDate

@Composable
fun AddEntryScreen(
    viewModel: EntryViewModel,
    navController: NavController,
) {
    BasePage(navController) {
        Column(Modifier.fillMaxWidth().padding(8.dp)) {
            Card(Modifier.padding(8.dp).fillMaxWidth()) {
                Text(
                    viewModel.selectedDate.value,
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    style = TextStyle(fontSize = 24.sp),
                    textAlign = TextAlign.Center
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
