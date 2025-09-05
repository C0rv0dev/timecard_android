package com.lucascouto.timecardapp.struct.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lucascouto.timecardapp.struct.data.daos.WorkdayDao
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity

@Database(
    entities = [
        WorkdayEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workdayDao(): WorkdayDao
}
