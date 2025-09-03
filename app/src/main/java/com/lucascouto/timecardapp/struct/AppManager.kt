package com.lucascouto.timecardapp.struct

class AppManager {
    // Global singleton
    private val instance: AppManager = this
    val shared by lazy { instance }

    fun boot() {
        // Initialize things like notifications, logging, etc.
    }
}
