package com.lucascouto.timecardapp.struct.data.storage

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.lucascouto.timecardapp.struct.PreferencesKeys
import com.lucascouto.timecardapp.struct.data.entities.SettingsEntity
import kotlinx.coroutines.flow.first
import androidx.core.net.toUri

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val Context.exportDataStore: DataStore<Preferences> by preferencesDataStore(name = "export_settings")

class DataStorageManager(private val context: Context) {
    suspend fun getSettings(): SettingsEntity {
        val preferences = context.dataStore.data
        var settings = SettingsEntity.default()

        val prefs = preferences.first()
        settings = SettingsEntity(
            defaultHourlyPay = prefs[PreferencesKeys.HOURLY_PAY] ?: settings.defaultHourlyPay,
            bonusPayment = prefs[PreferencesKeys.BONUS_PAYMENT] ?: settings.bonusPayment,
            overtimeRateMultiplier = prefs[PreferencesKeys.OVERTIME_RATE]
                ?: settings.overtimeRateMultiplier,
            lateNightRateMultiplier = prefs[PreferencesKeys.LATE_NIGHT_RATE]
                ?: settings.lateNightRateMultiplier,
            baseShiftDurationHours = prefs[PreferencesKeys.BASE_SHIFT_HOURS]
                ?: settings.baseShiftDurationHours,
            shiftStartTime = prefs[PreferencesKeys.SHIFT_START_TIME] ?: settings.shiftStartTime,
            shiftEndTime = prefs[PreferencesKeys.SHIFT_END_TIME] ?: settings.shiftEndTime,
            lateNightStartTime = prefs[PreferencesKeys.LATE_NIGHT_START_TIME]
                ?: settings.lateNightStartTime,
            lateNightEndTime = prefs[PreferencesKeys.LATE_NIGHT_END_TIME]
                ?: settings.lateNightEndTime,
            lunchStartTime = prefs[PreferencesKeys.LUNCH_START_TIME] ?: settings.lunchStartTime,
            lunchDurationMinutes = prefs[PreferencesKeys.LUNCH_DURATION_MINUTES]
                ?: settings.lunchDurationMinutes,
            receiveNotifications = prefs[PreferencesKeys.RECEIVE_NOTIFICATIONS]
                ?: settings.receiveNotifications,
            notificationTime = prefs[PreferencesKeys.NOTIFICATION_TIME] ?: settings.notificationTime
        )

        return settings
    }

    suspend fun saveSettings(settings: SettingsEntity) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HOURLY_PAY] = settings.defaultHourlyPay
            preferences[PreferencesKeys.BONUS_PAYMENT] = settings.bonusPayment
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
    }

    suspend fun saveExportPath(path: Uri) {
        context.exportDataStore.edit { preferences ->
            preferences[PreferencesKeys.EXPORT_PATH] = path.toString()
        }
    }

    suspend fun getExportPath(): Uri? {
        var uri: Uri? = null

        val preferences = context.exportDataStore.data.first()
        val pathString = preferences[PreferencesKeys.EXPORT_PATH]
        if (pathString != null) uri = pathString.toUri()

        return uri
    }
}
