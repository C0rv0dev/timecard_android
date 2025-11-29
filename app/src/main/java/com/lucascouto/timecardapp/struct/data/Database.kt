package com.lucascouto.timecardapp.struct.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.lucascouto.timecardapp.struct.data.daos.WorkdayDao
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity

@Database(
    entities = [WorkdayEntity::class],
    version = 2,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun workdayDao(): WorkdayDao

    class DatabaseAutoMigrations {
        companion object {
            var MIGRATION_1_2 = object : Migration(1, 2) {
                override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                    database.execSQL("ALTER TABLE workdays ADD COLUMN default_bonus_payment_at_time INTEGER NOT NULL DEFAULT 0")
                }
            }
        }
    }
}
