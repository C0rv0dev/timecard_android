package com.lucascouto.timecardapp.struct

import android.Manifest
import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lucascouto.timecardapp.struct.data.storage.DataStorageManager
import com.lucascouto.timecardapp.sys.config.BuildConfig

object PreferencesKeys {
    val HOURLY_PAY = intPreferencesKey("hourly_pay")
    val OVERTIME_RATE = intPreferencesKey("overtime_rate")
    val LATE_NIGHT_RATE = intPreferencesKey("late_night_rate")
    val BASE_SHIFT_HOURS = intPreferencesKey("base_shift_hours")
    val SHIFT_START_TIME = stringPreferencesKey("shift_start_time")
    val SHIFT_END_TIME = stringPreferencesKey("shift_end_time")
    val LATE_NIGHT_START_TIME = stringPreferencesKey("late_night_start_time")
    val LATE_NIGHT_END_TIME = stringPreferencesKey("late_night_end_time")
    val LUNCH_START_TIME = stringPreferencesKey("lunch_start_time")
    val LUNCH_DURATION_MINUTES = intPreferencesKey("lunch_duration_minutes")
    val RECEIVE_NOTIFICATIONS = booleanPreferencesKey("receive_notifications")
    val NOTIFICATION_TIME = stringPreferencesKey("notification_time")
}

class AppManager(private val context: Context) {
    // Global singleton
    private val instance: AppManager = this
    val shared by lazy { instance }

    private val _dataStorageManager: DataStorageManager by lazy { DataStorageManager(context) }
    val dataStorageManager: DataStorageManager by lazy { _dataStorageManager }

    // Debug
    val isInDebugMode: Boolean
        get() = BuildConfig.DEBUG

    // Boot function to initialize app-wide settings
    fun boot() {
        // Initialize things like notifications, logging, etc.
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as android.app.Activity,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1
            )
        }
    }
}
