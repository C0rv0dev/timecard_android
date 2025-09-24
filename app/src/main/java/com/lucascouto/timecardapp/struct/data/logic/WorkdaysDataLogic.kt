package com.lucascouto.timecardapp.struct.data.logic

import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity
import com.lucascouto.timecardapp.struct.data.enums.WorkdayTypeEnum
import com.lucascouto.timecardapp.struct.data.utils.TimeUtils

class WorkdaysDataLogic(private val workdays: List<WorkdayEntity>) {
    // Vars
    companion object {
        // Base
        val hourlyPay = 1000 // TODO: Fetch from settings
        val baseWorkdayHours = 8 // TODO: Fetch from settings

        // Rates
        val overtimeRateMultiplier = 25F / 100F // TODO: Fetch from settings
        val lateNightRateMultiplier = 25F / 100F // TODO: Fetch from settings
        val lateNightStart = "22:00" // TODO: Fetch from settings
        val lateNightEnd = "05:00" // TODO: Fetch from settings
    }

    // Methods
    fun calculateEstimatedSalary(): Int {
        var totalSalary = 0.0
        for (workday in workdays) {
            if (workday.shiftType != WorkdayTypeEnum.UNPAID_LEAVE.value)
                totalSalary += calculateDaySalary(workday)
        }

        return totalSalary.toInt()
    }

    fun calculateTotalWorkedHours(): Int {
        var totalWorkedMinutes = 0

        for (workday in workdays) {
            if (workday.shiftType == WorkdayTypeEnum.UNPAID_LEAVE.value) continue

            val startParts = TimeUtils.splitTime(workday.shiftStartHour) ?: continue
            val endParts = TimeUtils.splitTime(workday.shiftEndHour) ?: continue

            val startMinutes = startParts.first * 60 + startParts.second
            val endMinutes = endParts.first * 60 + endParts.second

            val workedMinutes =
                if (endMinutes >= startMinutes) endMinutes - startMinutes else (24 * 60 - startMinutes) + endMinutes

            totalWorkedMinutes += workedMinutes - workday.lunchDurationMinutes
        }

        return totalWorkedMinutes / 60
    }

    fun calculateTotalOvertimeHours(): Int {
        var totalOvertimeMinutes = 0

        for (workday in workdays) {
            if (workday.shiftType == WorkdayTypeEnum.UNPAID_LEAVE.value) continue

            val startParts = TimeUtils.splitTime(workday.shiftStartHour) ?: continue
            val endParts = TimeUtils.splitTime(workday.shiftEndHour) ?: continue

            val startMinutes = startParts.first * 60 + startParts.second
            val endMinutes = endParts.first * 60 + endParts.second
            val workedMinutes =
                if (endMinutes >= startMinutes) endMinutes - startMinutes else (24 * 60 - startMinutes) + endMinutes

            val effectiveWorkedMinutes = workedMinutes - workday.lunchDurationMinutes
            val overtimeMinutes = (effectiveWorkedMinutes - baseWorkdayHours * 60).coerceAtLeast(0)

            totalOvertimeMinutes += overtimeMinutes
        }

        return totalOvertimeMinutes / 60
    }

    fun calculateTotalWorkedDays(): Int {
        return workdays.count { it.shiftType == WorkdayTypeEnum.REGULAR.value }
    }

    // Utils
    private fun calculateDaySalary(workday: WorkdayEntity): Int {
        var salary = 0.0
        var workedMinutesSoFar = 0

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

            val isLateNight = isLateNight(minuteOfDay)
            val isOvertime = workedMinutesSoFar >= baseWorkdayHours * 60

            var multiplier = 1.0
            if (isOvertime) multiplier += overtimeRateMultiplier
            if (isLateNight) multiplier += lateNightRateMultiplier

            salary += hourlyPay * multiplier

            workedMinutesSoFar++
            current++
        }

        // Convert to hourly
        salary = salary / 60
        return salary.toInt()
    }

    private fun isLateNight(minuteOfDay: Int): Boolean {
        val lateStart = TimeUtils.convertTimeToMinutes(lateNightStart) ?: return false
        val lateEnd = TimeUtils.convertTimeToMinutes(lateNightEnd) ?: return false
        return if (lateStart < lateEnd) minuteOfDay in lateStart until lateEnd else { minuteOfDay >= lateStart || minuteOfDay < lateEnd }
    }
}
