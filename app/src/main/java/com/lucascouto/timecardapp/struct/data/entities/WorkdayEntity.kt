package com.lucascouto.timecardapp.struct.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workdays")
data class WorkdayEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "date") val date: String, // Stored as ISO-8601 string (e.g., "2023-10-05")
    @ColumnInfo(name = "shift_start_hour") val shiftStartHour: String, // Stored as "HH:mm" string (e.g., "09:00")
    @ColumnInfo(name = "shift_end_hour") val shiftEndHour: String, // Stored as "HH:mm" string (e.g., "17:00")
    @ColumnInfo(name = "shift_duration") val shiftDuration: String, // Stored as "HH:mm" string (e.g., "08:00")
    @ColumnInfo(name = "shift_type") val shiftType: String, // e.g., "Regular", "Overtime", etc.
)
