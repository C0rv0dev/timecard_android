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

    fun getWorkdayData(): Map<String, Int> {
        val data: MutableMap<String, Int> = mutableMapOf()

        // hours
        data to ("regular_hours" to 0)
        data to ("overtime_hours" to 0)
        data to ("late_night_hours" to 0)
        // salaries
        data to ("regular" to 0)
        data to ("bonus" to 0)
        data to ("overtime" to 0)
        data to ("late_night" to 0)
        data to ("allowances" to 0)

        if (shiftType == WorkdayTypeEnum.UNPAID_LEAVE.value || shiftType == WorkdayTypeEnum.HOLIDAY.value)
            return data
        else if (shiftType == WorkdayTypeEnum.PAID_LEAVE.value) {
            data["allowances"] = baseShiftDurationHoursAtTime * defaultHourlyPayAtTime
            return data
        }

        val shiftDurationMinutes = TimeUtils.convertTimeToMinutes(shiftDuration) ?: 0
        val baseShiftDurationMinutes = baseShiftDurationHoursAtTime * 60
        val regularHours = shiftDurationMinutes / 60

        // calc bonus payment
        // if shift is grater than base shift duration, bonus is only for base hours
        if (shiftDurationMinutes > baseShiftDurationMinutes) {
            val overtimeRate: Float = 1f + (overtimeRateMultiAtTime / 100f)
            val overtimeHours = (shiftDurationMinutes - baseShiftDurationMinutes) / 60

            data["bonus"] = baseShiftDurationHoursAtTime * defaultBonusPaymentAtTime
            data["regular"] = baseShiftDurationHoursAtTime * defaultHourlyPayAtTime
            data["overtime"] =
                ((defaultHourlyPayAtTime + defaultBonusPaymentAtTime) * overtimeHours * overtimeRate).toInt()

            data["overtime_hours"] = overtimeHours
            data["regular_hours"] = baseShiftDurationHoursAtTime
        } else {
            data["bonus"] = regularHours * defaultBonusPaymentAtTime
            data["regular"] = regularHours * defaultHourlyPayAtTime
            data["regular_hours"] = regularHours
        }

        val lateNightHours = calculateLateNightHours()
        val lateNightRate: Float = lateNightRateMultiAtTime / 100f

        data["late_night_hours"] = lateNightHours
        data["late_night"] =
            (lateNightHours * (defaultHourlyPayAtTime + defaultBonusPaymentAtTime) * lateNightRate).toInt()

        return data
    }

    private fun calculateLateNightHours(): Int {
        val shiftStartMinutes = TimeUtils.convertTimeToMinutes(shiftStartHour) ?: return 0
        val shiftEndMinutes = TimeUtils.convertTimeToMinutes(shiftEndHour) ?: return 0
        val lateNightStartMinutes =
            TimeUtils.convertTimeToMinutes(lateNightStartTimeAtTime) ?: return 0
        val lateNightEndMinutes = TimeUtils.convertTimeToMinutes(lateNightEndTimeAtTime) ?: return 0

        var totalLateNightMinutes = 0

        var currentMinute = shiftStartMinutes
        while (true) {
            val isInLateNight = if (lateNightStartMinutes < lateNightEndMinutes) {
                currentMinute in lateNightStartMinutes until lateNightEndMinutes
            } else {
                currentMinute >= lateNightStartMinutes || currentMinute < lateNightEndMinutes
            }

            if (isInLateNight) {
                totalLateNightMinutes++
            }

            if (currentMinute == shiftEndMinutes) break

            currentMinute = (currentMinute + 1) % (24 * 60)
        }

        return totalLateNightMinutes / 60
    }
}
