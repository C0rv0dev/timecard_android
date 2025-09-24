package com.lucascouto.timecardapp.struct.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lucascouto.timecardapp.struct.data.enums.WorkdayTypeEnum
import java.time.LocalDate

@Entity(tableName = "workdays")
data class WorkdayEntity(
    @PrimaryKey val id: Long? = null,
    @ColumnInfo(name = "date") val date: String, // Stored as ISO-8601 string (e.g., "2023-10-05")
    @ColumnInfo(name = "shift_start_hour") var shiftStartHour: String, // Stored as "HH:mm" string (e.g., "09:00")
    @ColumnInfo(name = "shift_end_hour") var shiftEndHour: String, // Stored as "HH:mm" string (e.g., "17:00")
    @ColumnInfo(name = "shift_duration") var shiftDuration: String, // Stored as "HH:mm" string (e.g., "08:00")
    @ColumnInfo(name = "shift_type") var shiftType: Int, // e.g., 1 "Regular", 2 "Overtime", etc.

    // New columns
    @ColumnInfo(name = "lunch_start_hour") var lunchStartHour: String, // Stored as "HH:mm" string (e.g., "12:00")
    @ColumnInfo(name = "lunch_duration_minutes") var lunchDurationMinutes: Int,
) {
    fun getLocalDate(): LocalDate {
        return LocalDate.parse(date)
    }

    companion object {
        fun default(id: Long? = null): WorkdayEntity {
            return WorkdayEntity(
                id = id,
                date = LocalDate.now().toString(),
                // Shift defaults
                shiftStartHour = "08:00",
                shiftEndHour = "17:00",
                shiftDuration = "08:00",
                shiftType = WorkdayTypeEnum.REGULAR.value,
                // Lunch defaults
                lunchStartHour = "12:00",
                lunchDurationMinutes = 60
            )
        }
    }
}
