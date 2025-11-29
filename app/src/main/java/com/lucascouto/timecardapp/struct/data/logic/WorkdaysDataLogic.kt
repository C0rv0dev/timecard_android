package com.lucascouto.timecardapp.struct.data.logic

import android.util.Log
import com.lucascouto.timecardapp.struct.data.DatabaseProvider
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity
import com.lucascouto.timecardapp.struct.data.enums.WorkdayTypeEnum
import com.lucascouto.timecardapp.struct.data.utils.TimeUtils
import org.json.JSONArray
import org.json.JSONObject

class WorkdaysDataLogic(private val workdays: List<WorkdayEntity>) {
    // Methods
    fun calculateEstimatedSalary(): Int {
        var totalSalary = 0.0
        for (workday in workdays) {
            if (workday.shiftType != WorkdayTypeEnum.UNPAID_LEAVE.value) totalSalary += calculateDaySalary(
                workday
            )
        }

        return totalSalary.toInt()
    }

    fun calculateEstimatedRegularSalary(): Int {
        var totalRegularSalary = 0.0
        for (workday in workdays) {
            val effectiveWorkedMinutes = getWorkedMinutesMinusLunch(workday)
            val regularMinutes =
                effectiveWorkedMinutes.coerceAtMost(workday.baseShiftDurationHoursAtTime * 60)

            totalRegularSalary += (regularMinutes.toDouble() / 60.0) * workday.defaultHourlyPayAtTime
        }

        return totalRegularSalary.toInt()
    }

    fun calculateEstimatedOvertimeSalary(): Int {
        var totalOvertimeSalary = 0.0
        for (workday in workdays) {
            val effectiveWorkedMinutes = getWorkedMinutesMinusLunch(workday)
            val overtimeMinutes =
                (effectiveWorkedMinutes - workday.baseShiftDurationHoursAtTime * 60).coerceAtLeast(0)

            totalOvertimeSalary += (overtimeMinutes.toDouble() / 60.0) * workday.defaultHourlyPayAtTime * (1 + workday.overtimeRateMultiAtTime.toFloat() / 100F)
        }

        return totalOvertimeSalary.toInt()
    }

    fun calculateEstimatedLateNightSalary(): Int {
        // basically, all that remains from total salary after regular and overtime
        val totalSalary = calculateEstimatedSalary()
        val regularSalary = calculateEstimatedRegularSalary()
        val overtimeSalary = calculateEstimatedOvertimeSalary()

        return totalSalary - regularSalary - overtimeSalary
    }

    fun calculateTotalWorkedHours(): Int {
        var totalWorkedMinutes = 0

        for (workday in workdays) {
            totalWorkedMinutes += getWorkedMinutesMinusLunch(workday)
        }

        return totalWorkedMinutes / 60
    }

    fun calculateTotalOvertimeHours(): Int {
        var totalOvertimeMinutes = 0

        for (workday in workdays) {
            val effectiveWorkedMinutes = getWorkedMinutesMinusLunch(workday)
            val overtimeMinutes =
                (effectiveWorkedMinutes - workday.baseShiftDurationHoursAtTime * 60).coerceAtLeast(0)

            totalOvertimeMinutes += overtimeMinutes
        }

        return totalOvertimeMinutes / 60
    }

    fun calculateTotalRegularHours(): Int {
        var totalRegularMinutes = 0

        for (workday in workdays) {
            val effectiveWorkedMinutes = getWorkedMinutesMinusLunch(workday)
            val regularMinutes =
                effectiveWorkedMinutes.coerceAtMost(workday.baseShiftDurationHoursAtTime * 60)

            totalRegularMinutes += regularMinutes
        }

        return totalRegularMinutes / 60
    }

    fun calculateTotalWorkedDays(): Int {
        return workdays.count { it.shiftType == WorkdayTypeEnum.REGULAR.value }
    }

    // Utils
    private fun calculateDaySalary(workday: WorkdayEntity): Int {
        var salary = 0.0
        var workedMinutesSoFar = 0

        if (workday.shiftType == WorkdayTypeEnum.PAID_LEAVE.value) {
            val totalShiftMinutes = TimeUtils.convertTimeToMinutes(
                TimeUtils.calculateDuration(
                    workday.shiftStartHour, workday.shiftEndHour, workday.lunchDurationMinutes
                )
            )

            val totalShiftHours = totalShiftMinutes?.div(60.0) ?: 0.0
            return (totalShiftHours * workday.defaultHourlyPayAtTime).toInt()
        }

        // Convert times to minutes
        val end = TimeUtils.convertTimeToMinutes(workday.shiftEndHour) ?: return 0
        val start = TimeUtils.convertTimeToMinutes(workday.shiftStartHour) ?: return 0

        // Handle overnight
        val shiftEnd = if (end <= start) end + 24 * 60 else end

        // Define break
        val lunchStart = TimeUtils.convertTimeToMinutes(workday.lunchStartHour) ?: return 0
        val lunchStartAbs = if (lunchStart < start) lunchStart + 24 * 60 else lunchStart
        val lunchEndAbs = lunchStartAbs + workday.lunchDurationMinutes

        // Calculate salary minute by minute
        var current = start
        while (current < shiftEnd) {
            if (current in lunchStartAbs until lunchEndAbs) {
                current++
                continue
            }

            val minuteOfDay = current % (24 * 60)

            val isLateNight = isLateNight(
                minuteOfDay, workday.lateNightStartTimeAtTime, workday.lateNightEndTimeAtTime
            )
            val isOvertime = workedMinutesSoFar >= workday.baseShiftDurationHoursAtTime * 60

            var multiplier = 1.0
            if (isOvertime) multiplier += workday.overtimeRateMultiAtTime.toFloat() / 100F
            if (isLateNight) multiplier += workday.lateNightRateMultiAtTime.toFloat() / 100F

            salary += workday.defaultHourlyPayAtTime * multiplier

            workedMinutesSoFar++
            current++
        }

        // Convert to hourly
        salary /= 60
        return salary.toInt()
    }

    private fun getWorkedMinutesMinusLunch(workday: WorkdayEntity): Int {
        if (workday.shiftType == WorkdayTypeEnum.UNPAID_LEAVE.value) return 0

        val startParts = TimeUtils.splitTime(workday.shiftStartHour) ?: return 0
        val endParts = TimeUtils.splitTime(workday.shiftEndHour) ?: return 0

        val startMinutes = startParts.first * 60 + startParts.second
        val endMinutes = endParts.first * 60 + endParts.second
        val workedMinutes =
            if (endMinutes >= startMinutes) endMinutes - startMinutes else (24 * 60 - startMinutes) + endMinutes

        return workedMinutes - workday.lunchDurationMinutes
    }

    private fun isLateNight(
        minuteOfDay: Int, lateNightStart: String, lateNightEnd: String
    ): Boolean {
        val lateStart = TimeUtils.convertTimeToMinutes(lateNightStart) ?: return false
        val lateEnd = TimeUtils.convertTimeToMinutes(lateNightEnd) ?: return false
        return if (lateStart < lateEnd) minuteOfDay in lateStart until lateEnd else {
            minuteOfDay >= lateStart || minuteOfDay < lateEnd
        }
    }
}
