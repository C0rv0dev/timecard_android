package com.lucascouto.timecardapp.struct

import com.lucascouto.timecardapp.sys.config.BuildConfig

class AppManager {
    // Global singleton
    private val instance: AppManager = this
    val shared by lazy { instance }

    // Debug
    val isInDebugMode: Boolean
        get() = BuildConfig.DEBUG

    // Boot function to initialize app-wide settings
    fun boot() {
        // Initialize things like notifications, logging, etc.
    }
}
