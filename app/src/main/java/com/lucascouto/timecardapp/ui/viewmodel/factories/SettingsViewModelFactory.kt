package com.lucascouto.timecardapp.ui.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucascouto.timecardapp.struct.data.storage.DataStorageManager
import com.lucascouto.timecardapp.ui.screens.Profile.sub.Settings.SettingsViewModel

class SettingsViewModelFactory(private val dataStorageManager: DataStorageManager): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(dataStorageManager) as T
    }
}
