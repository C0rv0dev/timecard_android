package com.lucascouto.timecardapp.struct.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workdays")
data class WorkdayEntity(
    @PrimaryKey val id: Long? = null,
    @ColumnInfo(name = "date") val date: String, // Stored as ISO-8601 string (e.g., "2023-10-05")
    @ColumnInfo(name = "shift_start_hour") var shiftStartHour: String, // Stored as "HH:mm" string (e.g., "09:00")
    @ColumnInfo(name = "shift_end_hour") var shiftEndHour: String, // Stored as "HH:mm" string (e.g., "17:00")
    @ColumnInfo(name = "shift_duration") var shiftDuration: String, // Stored as "HH:mm" string (e.g., "08:00")
    @ColumnInfo(name = "shift_type") var shiftType: Int, // e.g., 1 "Regular", 2 "Overtime", etc.
) {
    companion object {
        fun default(id: Long? = null): WorkdayEntity {
            return WorkdayEntity(
                id = id,
                date = LocalDate.now().toString(),
                shiftStartHour = "08:00",
                shiftEndHour = "17:00",
                shiftDuration = "08:00",
                shiftType = 1
            )
        }
    }
}
