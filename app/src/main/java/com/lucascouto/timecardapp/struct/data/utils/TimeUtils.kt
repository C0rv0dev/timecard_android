package com.lucascouto.timecardapp.struct.data.utils

class TimeUtils {
    companion object {
        fun splitTime(time: String): Pair<Int, Int>? {
            val parts = time.split(":").mapNotNull { it.toIntOrNull() }
            if (parts.size != 2) return null
            return Pair(parts[0], parts[1])
        }

        fun convertTimeToMinutes(time: String): Int? {
            val parts = time.split(":").mapNotNull { it.toIntOrNull() }
            if (parts.size != 2) return null
            return parts[0] * 60 + parts[1]
        }

        fun convertMinutesToTime(minutes: Int): String {
            val hrs = minutes / 60
            val mins = minutes % 60
            return String.format("%02d:%02d", hrs, mins)
        }

        fun calculateDuration(start: String, end: String, lunchDuration: Int): String {
            val startParts = start.split(":")
            val endParts = end.split(":")

            if (startParts.size != 2 || endParts.size != 2) return ""

            val startHour = startParts[0].toIntOrNull() ?: return ""
            val startMinute = startParts[1].toIntOrNull() ?: return ""
            val endHour = endParts[0].toIntOrNull() ?: return ""
            val endMinute = endParts[1].toIntOrNull() ?: return ""

            var totalStartMinutes = startHour * 60 + startMinute
            var totalEndMinutes = endHour * 60 + endMinute

            if (totalEndMinutes < totalStartMinutes) {
                totalEndMinutes += 24 * 60
            }

            // remove lunch break
            totalEndMinutes -= lunchDuration

            val durationMinutes = totalEndMinutes - totalStartMinutes
            val durationHours = durationMinutes / 60
            val durationRemainingMinutes = durationMinutes % 60

            val hoursString = if (durationHours < 10) "0$durationHours" else "$durationHours"
            val minutesString =
                if (durationRemainingMinutes < 10) "0$durationRemainingMinutes" else "$durationRemainingMinutes"

            return "$hoursString:$minutesString"
        }

        fun parseTime(time: String): String {
            val parts = time.split(":")

            if (parts.size != 2) return ""

            val hour = parts[0].toIntOrNull() ?: return ""
            val minute = parts[1].toIntOrNull() ?: return ""

            val hourString = if (hour < 10) "0$hour" else "$hour"
            val minuteString = if (minute < 10) "0$minute" else "$minute"

            return "$hourString:$minuteString"
        }

        fun parseDate(date: String): String {
            val parts = date.split("-")

            if (parts.size != 3) return ""

            val year = parts[0].toIntOrNull() ?: return ""
            val month = parts[1].toIntOrNull() ?: return ""
            val day = parts[2].toIntOrNull() ?: return ""

            val dayString = if (day < 10) "0$day" else "$day"
            val monthString = if (month < 10) "0$month" else "$month"
            val yearString = "$year"

            return "$dayString/$monthString/$yearString"
        }

        fun unparseDate(date: String): String {
            val parts = date.split("/")

            if (parts.size != 3) return ""

            val day = parts[0].toIntOrNull() ?: return ""
            val month = parts[1].toIntOrNull() ?: return ""
            val year = parts[2].toIntOrNull() ?: return ""

            val dayString = if (day < 10) "0$day" else "$day"
            val monthString = if (month < 10) "0$month" else "$month"
            val yearString = "$year"

            return "$yearString-$monthString-$dayString"
        }
    }
}
