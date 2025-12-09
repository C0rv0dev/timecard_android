package com.lucascouto.timecardapp.struct.data.entities

import com.lucascouto.timecardapp.struct.data.enums.WorkdayTypeEnum

enum class WorkdayCalcs {
    REGULAR,
    BONUS,
    OVERTIME,
    LATE_NIGHT,
    LOCOMOTION,
    ALLOWANCE,
}

data class WorkdayDataEntity(
    var entity: WorkdayEntity,
    var regularHours: Float,
    var overtimeHours: Float,
    var lateNightHours: Float,
    var allowance: Boolean,
) {
    fun calc(type: WorkdayCalcs, rate: Int = 0): Int {
        return when (type) {
            WorkdayCalcs.REGULAR -> calculateRegularHours()
            WorkdayCalcs.BONUS -> calculateBonusPayment()
            WorkdayCalcs.OVERTIME -> calculateOvertimeHours()
            WorkdayCalcs.LATE_NIGHT -> calculateLateNightHours()
            WorkdayCalcs.LOCOMOTION -> calculateLocomotionAllowance(rate)
            WorkdayCalcs.ALLOWANCE -> calculateAllowance()
        }
    }

    private fun calculateRegularHours(): Int {
        return (regularHours * entity.defaultHourlyPayAtTime).toInt()
    }

    private fun calculateBonusPayment(): Int {
        return (regularHours * entity.defaultBonusPaymentAtTime).toInt()
    }

    private fun calculateOvertimeHours(): Int {
        val overtimeRate: Float = (entity.overtimeRateMultiAtTime / 100f) + 1
        val overtimePay = entity.defaultHourlyPayAtTime + entity.defaultBonusPaymentAtTime

        return (overtimeHours * (overtimePay * overtimeRate)).toInt()
    }

    private fun calculateLateNightHours(): Int {
        val lateNightRate: Float = entity.lateNightRateMultiAtTime / 100f
        val lateNightPay = entity.defaultHourlyPayAtTime + entity.defaultBonusPaymentAtTime

        return (lateNightHours * (lateNightPay * lateNightRate)).toInt()
    }

    private fun calculateLocomotionAllowance(rate: Int): Int {
        if (rate <= 0)
            return 0
        if (entity.shiftType != WorkdayTypeEnum.REGULAR.value && entity.shiftType != WorkdayTypeEnum.PAID_LEAVE.value)
            return 0

        return rate
    }

    private fun calculateAllowance(): Int {
        return (entity.baseShiftDurationHoursAtTime * entity.defaultHourlyPayAtTime)
    }
}
