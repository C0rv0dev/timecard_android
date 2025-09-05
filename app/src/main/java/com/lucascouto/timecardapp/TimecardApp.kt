package com.lucascouto.timecardapp

import android.app.Application
import com.lucascouto.timecardapp.struct.data.DatabaseProvider

class TimecardApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Boot the AppManager
        DatabaseProvider.boot(this)
    }
}
