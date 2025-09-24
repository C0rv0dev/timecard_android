package com.lucascouto.timecardapp.struct.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.lucascouto.timecardapp.struct.PreferencesKeys
import com.lucascouto.timecardapp.struct.data.entities.SettingsEntity
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStorageManager(private val context: Context) {
    suspend fun getSettings(): SettingsEntity {
        val preferences = context.dataStore.data
        var settings = SettingsEntity.default()

        val prefs = preferences.first()
        settings = SettingsEntity(
            defaultHourlyPay = prefs[PreferencesKeys.HOURLY_PAY] ?: settings.defaultHourlyPay,
            overtimeRateMultiplier = prefs[PreferencesKeys.OVERTIME_RATE] ?: settings.overtimeRateMultiplier,
            lateNightRateMultiplier = prefs[PreferencesKeys.LATE_NIGHT_RATE] ?: settings.lateNightRateMultiplier,
            baseShiftDurationHours = prefs[PreferencesKeys.BASE_SHIFT_HOURS] ?: settings.baseShiftDurationHours,
            shiftStartTime = prefs[PreferencesKeys.SHIFT_START_TIME] ?: settings.shiftStartTime,
            shiftEndTime = prefs[PreferencesKeys.SHIFT_END_TIME] ?: settings.shiftEndTime,
            lateNightStartTime = prefs[PreferencesKeys.LATE_NIGHT_START_TIME] ?: settings.lateNightStartTime,
            lateNightEndTime = prefs[PreferencesKeys.LATE_NIGHT_END_TIME] ?: settings.lateNightEndTime,
            lunchStartTime = prefs[PreferencesKeys.LUNCH_START_TIME] ?: settings.lunchStartTime,
            lunchDurationMinutes = prefs[PreferencesKeys.LUNCH_DURATION_MINUTES] ?: settings.lunchDurationMinutes,
            receiveNotifications = prefs[PreferencesKeys.RECEIVE_NOTIFICATIONS] ?: settings.receiveNotifications,
            notificationTime = prefs[PreferencesKeys.NOTIFICATION_TIME] ?: settings.notificationTime
        )

        return settings
    }

    suspend fun saveSettings(settings: SettingsEntity) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HOURLY_PAY] = settings.defaultHourlyPay
            preferences[PreferencesKeys.OVERTIME_RATE] = settings.overtimeRateMultiplier
            preferences[PreferencesKeys.LATE_NIGHT_RATE] = settings.lateNightRateMultiplier
            preferences[PreferencesKeys.BASE_SHIFT_HOURS] = settings.baseShiftDurationHours
            preferences[PreferencesKeys.SHIFT_START_TIME] = settings.shiftStartTime
            preferences[PreferencesKeys.SHIFT_END_TIME] = settings.shiftEndTime
            preferences[PreferencesKeys.LATE_NIGHT_START_TIME] = settings.lateNightStartTime
            preferences[PreferencesKeys.LATE_NIGHT_END_TIME] = settings.lateNightEndTime
            preferences[PreferencesKeys.LUNCH_START_TIME] = settings.lunchStartTime
            preferences[PreferencesKeys.LUNCH_DURATION_MINUTES] = settings.lunchDurationMinutes
            preferences[PreferencesKeys.RECEIVE_NOTIFICATIONS] = settings.receiveNotifications
            preferences[PreferencesKeys.NOTIFICATION_TIME] = settings.notificationTime
        }

        println("Saved Settings: $settings")
    }
}
