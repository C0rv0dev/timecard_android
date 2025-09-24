package com.lucascouto.timecardapp.ui.screens.Profile.sub.Settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascouto.timecardapp.struct.data.entities.SettingsEntity
import com.lucascouto.timecardapp.struct.data.storage.DataStorageManager
import com.lucascouto.timecardapp.struct.data.utils.TimeUtils
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStorageManager: DataStorageManager): ViewModel() {
    // vars
    private val _defaultHourlyPay: MutableState<Int> = mutableIntStateOf(0)
    val defaultHourlyPay by lazy { _defaultHourlyPay }

    private val _defaultOvertimeRateMultiplier: MutableState<Int> = mutableIntStateOf(0)
    val defaultOvertimeRate by lazy { _defaultOvertimeRateMultiplier }

    private val _lateNightRateMultiplier: MutableState<Int> = mutableIntStateOf(0)
    val lateNightRate by lazy { _lateNightRateMultiplier }

    private val _baseShiftDurationHours: MutableState<Int> = mutableIntStateOf(0)
    val baseShiftDurationHours by lazy { _baseShiftDurationHours }

    private val _shiftStartTime: MutableState<String> = mutableStateOf("")
    val shiftStartTime by lazy { _shiftStartTime }

    private val _shiftEndTime: MutableState<String> = mutableStateOf("")
    val shiftEndTime by lazy { _shiftEndTime }

    private val _lateNightStartTime: MutableState<String> = mutableStateOf("")
    val lateNightStartTime by lazy { _lateNightStartTime }

    private val _lateNightEndTime: MutableState<String> = mutableStateOf("")
    val lateNightEndTime by lazy { _lateNightEndTime }

    private val _lunchStartTime: MutableState<String> = mutableStateOf("")
    val lunchStartTime by lazy { _lunchStartTime }

    private val _lunchDurationMinutes: MutableState<Int> = mutableIntStateOf(0)
    val lunchDurationMinutes by lazy { _lunchDurationMinutes }

    private val _receiveNotifications: MutableState<Boolean> = mutableStateOf(false)
    val receiveNotifications by lazy { _receiveNotifications }

    private val _notificationTime: MutableState<String> = mutableStateOf("")
    val notificationTime by lazy { _notificationTime }

    // ui vars
    var _isEditing: MutableState<Boolean> = mutableStateOf(false)
    val isEditing by lazy { _isEditing }

    // Boot
    init {
        viewModelScope.launch {
            loadSettings()
        }
    }

    // Update Methods
    fun updateHourlyPay(newPay: String) {
        val pay = newPay.toIntOrNull() ?: 0
        _defaultHourlyPay.value = pay
    }

    fun updateOvertimeRate(newRate: String) {
        val rate = newRate.toIntOrNull() ?: 0
        _defaultOvertimeRateMultiplier.value = rate
    }

    fun updateLateNightRate(newRate: String) {
        val rate = newRate.toIntOrNull() ?: 0
        _lateNightRateMultiplier.value = rate
    }

    fun updateBaseShiftDuration(newDuration: String) {
        val duration = newDuration.toIntOrNull() ?: 0
        _baseShiftDurationHours.value = duration
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateShiftStartTime(newTime: TimePickerState) {
        _shiftStartTime.value = TimeUtils.parseTime("${newTime.hour}:${newTime.minute}")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateShiftEndTime(newTime: TimePickerState) {
        _shiftEndTime.value = TimeUtils.parseTime("${newTime.hour}:${newTime.minute}")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateLateNightStartTime(newTime: TimePickerState) {
        _lateNightStartTime.value = TimeUtils.parseTime("${newTime.hour}:${newTime.minute}")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateLateNightEndTime(newTime: TimePickerState) {
        _lateNightEndTime.value = TimeUtils.parseTime("${newTime.hour}:${newTime.minute}")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateLunchStartTime(newTime: TimePickerState) {
        _lunchStartTime.value = TimeUtils.parseTime("${newTime.hour}:${newTime.minute}")
    }

    fun updateLunchDuration(newDuration: String) {
        val duration = newDuration.toIntOrNull() ?: 0
        _lunchDurationMinutes.value = duration
    }

    // Methods
    fun loadSettings() {
        viewModelScope.launch {
            val settings = dataStorageManager.getSettings()

            _defaultHourlyPay.value = settings.defaultHourlyPay
            _defaultOvertimeRateMultiplier.value = settings.overtimeRateMultiplier
            _lateNightRateMultiplier.value = settings.lateNightRateMultiplier
            _baseShiftDurationHours.value = settings.baseShiftDurationHours
            _shiftStartTime.value = settings.shiftStartTime
            _shiftEndTime.value = settings.shiftEndTime
            _lateNightStartTime.value = settings.lateNightStartTime
            _lateNightEndTime.value = settings.lateNightEndTime
            _lunchStartTime.value = settings.lunchStartTime
            _lunchDurationMinutes.value = settings.lunchDurationMinutes
            _receiveNotifications.value = settings.receiveNotifications
            _notificationTime.value = settings.notificationTime
        }
    }

    fun saveSettings() {
        val settings = SettingsEntity(
            defaultHourlyPay = _defaultHourlyPay.value,
            overtimeRateMultiplier = _defaultOvertimeRateMultiplier.value,
            lateNightRateMultiplier = _lateNightRateMultiplier.value,
            baseShiftDurationHours = _baseShiftDurationHours.value,
            shiftStartTime = _shiftStartTime.value,
            shiftEndTime = _shiftEndTime.value,
            lateNightStartTime = _lateNightStartTime.value,
            lateNightEndTime = _lateNightEndTime.value,
            lunchStartTime = _lunchStartTime.value,
            lunchDurationMinutes = _lunchDurationMinutes.value,
            receiveNotifications = _receiveNotifications.value,
            notificationTime = _notificationTime.value
        )

        viewModelScope.launch {
            dataStorageManager.saveSettings(settings)
        }
    }

    fun resetToDefault() {
        val defaultSettings = SettingsEntity.default()

        _defaultHourlyPay.value = defaultSettings.defaultHourlyPay
        _defaultOvertimeRateMultiplier.value = defaultSettings.overtimeRateMultiplier
        _lateNightRateMultiplier.value = defaultSettings.lateNightRateMultiplier
        _baseShiftDurationHours.value = defaultSettings.baseShiftDurationHours
        _shiftStartTime.value = defaultSettings.shiftStartTime
        _shiftEndTime.value = defaultSettings.shiftEndTime
        _lateNightStartTime.value = defaultSettings.lateNightStartTime
        _lateNightEndTime.value = defaultSettings.lateNightEndTime
        _lunchStartTime.value = defaultSettings.lunchStartTime
        _lunchDurationMinutes.value = defaultSettings.lunchDurationMinutes
        _receiveNotifications.value = defaultSettings.receiveNotifications
        _notificationTime.value = defaultSettings.notificationTime

        // Save to database or shared preferences here
        println("Settings reset to default: $defaultSettings")
    }
}
