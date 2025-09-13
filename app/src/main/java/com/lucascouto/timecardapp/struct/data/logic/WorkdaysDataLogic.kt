package com.lucascouto.timecardapp.struct.data.logic

import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity

class WorkdaysDataLogic(private val workdays: List<WorkdayEntity>) {
    // Vars
    companion object {
        // Base
        val hourlyPay = 1000 // TODO: Fetch from settings
        val baseWorkdayHours = 8 // TODO: Fetch from settings

        // Lunch
        val lunchStartHour = "21:00" // TODO: Fetch from settings
        val lunchBreakMinutes = 60 // TODO: Fetch from settings

        // Rates
        val overtimeRateMultiplier = .25 // TODO: Fetch from settings
        val lateNightRateMultiplier = .25 // TODO: Fetch from settings
        val lateNightStart = "22:00" // TODO: Fetch from settings
        val lateNightEnd = "05:00" // TODO: Fetch from settings
    }

    // Methods
    fun calculateEstimatedSalary(): Int {
        var totalSalary = 0.0
        for (workday in workdays) totalSalary += calculateDaySalary(workday)
        return totalSalary.toInt()
    }

    fun calculateTotalWorkedHours(): Int {
        var totalWorkedMinutes = 0

        for (workday in workdays) {
            val startParts = splitTime(workday.shiftStartHour) ?: continue
            val endParts = splitTime(workday.shiftEndHour) ?: continue

            val startMinutes = startParts.first * 60 + startParts.second
            val endMinutes = endParts.first * 60 + endParts.second
            val workedMinutes =
                if (endMinutes >= startMinutes) endMinutes - startMinutes else (24 * 60 - startMinutes) + endMinutes

            totalWorkedMinutes += workedMinutes - lunchBreakMinutes
        }

        return totalWorkedMinutes / 60
    }

    fun calculateTotalOvertimeHours(): Int {
        var totalOvertimeMinutes = 0

        for (workday in workdays) {
            val startParts = splitTime(workday.shiftStartHour) ?: continue
            val endParts = splitTime(workday.shiftEndHour) ?: continue

            val startMinutes = startParts.first * 60 + startParts.second
            val endMinutes = endParts.first * 60 + endParts.second
            val workedMinutes =
                if (endMinutes >= startMinutes) endMinutes - startMinutes else (24 * 60 - startMinutes) + endMinutes

            val effectiveWorkedMinutes = workedMinutes - lunchBreakMinutes
            val overtimeMinutes = (effectiveWorkedMinutes - baseWorkdayHours * 60).coerceAtLeast(0)

            totalOvertimeMinutes += overtimeMinutes
        }

        return totalOvertimeMinutes / 60
    }

    fun calculateTotalWorkedDays(): Int {
        return workdays.size
    }

    // Utils
    private fun calculateDaySalary(workday: WorkdayEntity): Int {
        var salary = 0.0
        var workedMinutesSoFar = 0

        // Convert times to minutes
        val end = convertTimeToMinutes(workday.shiftEndHour) ?: return 0
        val start = convertTimeToMinutes(workday.shiftStartHour) ?: return 0

        // Handle overnight
        val shiftEnd = if (end <= start) end + 24 * 60 else end

        // Define break
        val lunchStart = convertTimeToMinutes(lunchStartHour) ?: return 0
        val lunchStartAbs = if (lunchStart < start) lunchStart + 24 * 60 else lunchStart
        val lunchEndAbs = lunchStartAbs + lunchBreakMinutes

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
        val lateStart = convertTimeToMinutes(lateNightStart) ?: return false
        val lateEnd = convertTimeToMinutes(lateNightEnd) ?: return false
        return if (lateStart < lateEnd) minuteOfDay in lateStart until lateEnd else { minuteOfDay >= lateStart || minuteOfDay < lateEnd }
    }

    private fun splitTime(time: String): Pair<Int, Int>? {
        val parts = time.split(":").mapNotNull { it.toIntOrNull() }
        if (parts.size != 2) return null
        return Pair(parts[0], parts[1])
    }

    private fun convertTimeToMinutes(time: String): Int? {
        val parts = time.split(":").mapNotNull { it.toIntOrNull() }
        if (parts.size != 2) return null
        return parts[0] * 60 + parts[1]
    }
}
