package com.lucascouto.timecardapp.struct.data

import android.content.Context
import androidx.room.Room
import com.lucascouto.timecardapp.struct.data.repositories.WorkdayRepository
import com.lucascouto.timecardapp.sys.config.BuildConfig

object DatabaseProvider {
    private lateinit var database: AppDatabase
    val workdayRepository: WorkdayRepository by lazy { WorkdayRepository(database.workdayDao()) }

    fun boot(context: Context) {
        database = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            BuildConfig.databaseName
        ).build()
    }
}