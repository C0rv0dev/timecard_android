package com.lucascouto.timecardapp.struct.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lucascouto.timecardapp.struct.data.enums.WorkdayTypeEnum
import com.lucascouto.timecardapp.struct.data.utils.TimeUtils
import java.time.LocalDate

@Entity(tableName = "workdays")
data class WorkdayEntity(
    @PrimaryKey val id: Long? = null,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "shift_start_hour") var shiftStartHour: String,
    @ColumnInfo(name = "shift_end_hour") var shiftEndHour: String,
    @ColumnInfo(name = "shift_duration") var shiftDuration: String,
    @ColumnInfo(name = "shift_type") var shiftType: Int,
    // Shift Details - to avoid read issues when Settings change
    @ColumnInfo(name = "default_hourly_pay_at_time") var defaultHourlyPayAtTime: Int,
    @ColumnInfo(name = "default_bonus_payment_at_time") var defaultBonusPaymentAtTime: Int,
    @ColumnInfo(name = "overtime_rate_multi_at_time") var overtimeRateMultiAtTime: Int,
    @ColumnInfo(name = "late_night_rate_multi_at_time") var lateNightRateMultiAtTime: Int,
    @ColumnInfo(name = "base_shift_duration_hours_at_time") var baseShiftDurationHoursAtTime: Int,
    @ColumnInfo(name = "late_nihgt_start_time_at_time") var lateNightStartTimeAtTime: String,
    @ColumnInfo(name = "late_night_end_time_at_time") var lateNightEndTimeAtTime: String,
    // Lunch details
    @ColumnInfo(name = "lunch_start_hour") var lunchStartHour: String,
    @ColumnInfo(name = "lunch_duration_minutes") var lunchDurationMinutes: Int,
) {

    companion object {
        fun default(id: Long? = null, localSettings: SettingsEntity? = null): WorkdayEntity {
            return WorkdayEntity(
                id = id,
                date = LocalDate.now().toString(),
                // Shift defaults
                shiftStartHour = localSettings?.shiftStartTime ?: "17:00",
                shiftEndHour = localSettings?.shiftEndTime ?: "02:00",
                shiftDuration = TimeUtils.calculateDuration(
                    localSettings?.shiftStartTime ?: "17:00",
                    localSettings?.shiftEndTime ?: "02:00",
                    localSettings?.lunchDurationMinutes ?: 60
                ),
                shiftType = WorkdayTypeEnum.REGULAR.value,
                // Shift details defaults
                defaultHourlyPayAtTime = localSettings?.defaultHourlyPay ?: 1000,
                defaultBonusPaymentAtTime = localSettings?.bonusPayment ?: 0,
                overtimeRateMultiAtTime = localSettings?.overtimeRateMultiplier ?: 25,
                lateNightRateMultiAtTime = localSettings?.lateNightRateMultiplier ?: 25,
                baseShiftDurationHoursAtTime = localSettings?.baseShiftDurationHours ?: 8,
                lateNightStartTimeAtTime = localSettings?.lateNightStartTime ?: "22:00",
                lateNightEndTimeAtTime = localSettings?.lateNightEndTime ?: "05:00",
                // Lunch defaults
                lunchStartHour = localSettings?.lunchStartTime ?: "21:00",
                lunchDurationMinutes = localSettings?.lunchDurationMinutes ?: 60,
            )
        }
    }

    fun getLocalDate(): LocalDate {
        return LocalDate.parse(date)
    }

    fun getWorkdayTypeColor(): Int {
        val color = when (shiftType) {
            WorkdayTypeEnum.REGULAR.value -> {
                val shiftDuration = TimeUtils.convertTimeToMinutes(shiftDuration)
                val baseShiftDurationHoursAtTime = baseShiftDurationHoursAtTime * 60

                shiftDuration?.let {
                    if (it < baseShiftDurationHoursAtTime) {
                        0xFFFFA000.toInt()
                    } else if (it > baseShiftDurationHoursAtTime) {
                        0xFF1261A0.toInt()
                    } else {
                        WorkdayTypeEnum.color(shiftType)
                    }
                }
            }

            else -> null
        }

        return color ?: WorkdayTypeEnum.color(shiftType)
    }

    fun getHours(): WorkdayDataEntity {
        val data = WorkdayDataEntity(
            entity = this,
            regularHours = 0f,
            overtimeHours = 0f,
            lateNightHours = 0f,
            allowance = false,
        )

        if (shiftType == WorkdayTypeEnum.UNPAID_LEAVE.value || shiftType == WorkdayTypeEnum.HOLIDAY.value)
            return data

        if (shiftType == WorkdayTypeEnum.PAID_LEAVE.value) {
            data.allowance = true
            return data
        }

        val shiftDurationMinutes = TimeUtils.convertTimeToMinutes(shiftDuration) ?: 0
        val baseShiftDurationMinutes = baseShiftDurationHoursAtTime * 60
        val regularHours = shiftDurationMinutes / 60f

        // calc regular hours
        data.regularHours = if (shiftDurationMinutes <= baseShiftDurationMinutes)
            regularHours
        else
            baseShiftDurationHoursAtTime.toFloat()

        // calc overtime hours
        data.overtimeHours = if (shiftDurationMinutes > baseShiftDurationMinutes)
            (shiftDurationMinutes - baseShiftDurationMinutes) / 60f
        else
            0f

        // calc late night hours
        data.lateNightHours = calculateLateNightMinutes() / 60f

        return data
    }

    private fun calculateLateNightMinutes(): Float {
        val lateNightStart = TimeUtils.convertTimeToMinutes(lateNightStartTimeAtTime) ?: 0
        val lateNightEnd = TimeUtils.convertTimeToMinutes(lateNightEndTimeAtTime) ?: 0
        val shiftStart = TimeUtils.convertTimeToMinutes(shiftStartHour) ?: 0
        val shiftEnd = TimeUtils.convertTimeToMinutes(shiftEndHour) ?: 0

        var lateNightMinutes = 0f

        // Handle shifts that cross midnight
        val adjustedShiftEnd = if (shiftEnd <= shiftStart) shiftEnd + 1440 else shiftEnd
        val adjustedLateNightEnd =
            if (lateNightEnd <= lateNightStart) lateNightEnd + 1440 else lateNightEnd

        // Calculate overlap with late night period
        val overlapStart = maxOf(shiftStart, lateNightStart)
        val overlapEnd = minOf(adjustedShiftEnd, adjustedLateNightEnd)

        // Calculate late night minutes
        if (overlapEnd > overlapStart) {
            lateNightMinutes = (overlapEnd - overlapStart).toFloat()
        }

        // Subtract lunch break if it falls within late night hours
        val lunchStart = TimeUtils.convertTimeToMinutes(lunchStartHour) ?: 0
        val lunchEnd = lunchStart + lunchDurationMinutes

        val adjustedLunchEnd = if (lunchEnd <= lunchStart) lunchEnd + 1440 else lunchEnd
        val lunchOverlapStart = maxOf(lunchStart, overlapStart)
        val lunchOverlapEnd = minOf(adjustedLunchEnd, overlapEnd)

        if (lunchOverlapEnd > lunchOverlapStart) {
            lateNightMinutes -= (lunchOverlapEnd - lunchOverlapStart).toFloat()
        }

        // Ensure late night minutes is not negative
        if (lateNightMinutes < 0f) lateNightMinutes = 0f

        return lateNightMinutes
    }
}
