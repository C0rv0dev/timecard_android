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
    @ColumnInfo(name = "default_bonus_payment_at_time") var defaultBonusPaymentAtTime: Int = 0,
    @ColumnInfo(name = "overtime_rate_multi_at_time") var overtimeRateMultiAtTime: Int,
    @ColumnInfo(name = "late_night_rate_multi_at_time") var lateNightRateMultiAtTime: Int,
    @ColumnInfo(name = "base_shift_duration_hours_at_time") var baseShiftDurationHoursAtTime: Int,
    @ColumnInfo(name = "late_nihgt_start_time_at_time") var lateNightStartTimeAtTime: String,
    @ColumnInfo(name = "late_night_end_time_at_time") var lateNightEndTimeAtTime: String,
    // Lunch details
    @ColumnInfo(name = "lunch_start_hour") var lunchStartHour: String,
    @ColumnInfo(name = "lunch_duration_minutes") var lunchDurationMinutes: Int,
) {
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
}
