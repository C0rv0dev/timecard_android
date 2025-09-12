package com.lucascouto.timecardapp.struct.data.enums

enum class WorkdayTypeEnum(val value: Int) {
    REGULAR(1),
    PAID_LEAVE(2),
    UNPAID_LEAVE(3);

    companion object {
        fun list(): List<Pair<String, Int>> {
            return entries.map { Pair(label(it), it.value) }
        }

        fun from(type: Int?): String {
            return when (type) {
                REGULAR.value -> label(REGULAR)
                PAID_LEAVE.value -> label(PAID_LEAVE)
                UNPAID_LEAVE.value -> label(UNPAID_LEAVE)
                else -> "Select an option"
            }
        }

        fun label(type: WorkdayTypeEnum): String {
            return when (type) {
                REGULAR -> "Regular"
                PAID_LEAVE -> "Paid Leave"
                UNPAID_LEAVE -> "Unpaid Leave"
            }
        }
    }
}
