package com.lucascouto.timecardapp.struct.data.enums

enum class WorkdayTypeEnum(val value: Int) {
    REGULAR(1),
    PAID_LEAVE(2),
    UNPAID_LEAVE(3),
    HOLIDAY(4);

    companion object {
        fun list(): List<Pair<String, Int>> {
            return entries.map { Pair(label(it), it.value) }
        }

        fun from(type: Int?): String {
            return when (type) {
                REGULAR.value -> label(REGULAR)
                PAID_LEAVE.value -> label(PAID_LEAVE)
                UNPAID_LEAVE.value -> label(UNPAID_LEAVE)
                HOLIDAY.value -> label(HOLIDAY)
                else -> "Select an option"
            }
        }

        fun label(type: WorkdayTypeEnum): String {
            return when (type) {
                REGULAR -> "Regular"
                PAID_LEAVE -> "Paid Leave"
                UNPAID_LEAVE -> "Unpaid Leave"
                HOLIDAY -> "Holiday"
            }
        }

        fun color(type: Int?): Int {
            return when (type) {
                REGULAR.value -> 0xFF4CAF50
                PAID_LEAVE.value -> 0xFF2196F3
                UNPAID_LEAVE.value -> 0xFFF44336
                HOLIDAY.value -> 0xFFF491F2
                else -> 0xFF9E9E9E
            }.toInt()
        }
    }
}
