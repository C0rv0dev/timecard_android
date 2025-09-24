package com.lucascouto.timecardapp.ui.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucascouto.timecardapp.ui.screens.Entry.EntryViewModel

class EntryViewModelFactory(private val date: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EntryViewModel().boot(date) as T
    }
}
