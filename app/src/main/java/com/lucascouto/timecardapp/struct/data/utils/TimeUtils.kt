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
    }
}
