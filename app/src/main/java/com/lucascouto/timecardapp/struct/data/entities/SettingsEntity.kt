package com.lucascouto.timecardapp.struct.data.entities

data class SettingsEntity(
    // Default values
    var defaultHourlyPay: Int,
    var bonusPayment: Int,
    var overtimeRateMultiplier: Int,
    var lateNightRateMultiplier: Int,
    // Shift details
    var baseShiftDurationHours: Int,
    var shiftStartTime: String,
    var shiftEndTime: String,
    var lateNightStartTime: String,
    var lateNightEndTime: String,
    // Lunch details
    var lunchStartTime: String,
    var lunchDurationMinutes: Int,
    // System settings
    var receiveNotifications: Boolean,
    var notificationTime: String,
) {
    companion object {
        fun default(): SettingsEntity {
            return SettingsEntity(
                defaultHourlyPay = 1000,
                bonusPayment = 0,
                overtimeRateMultiplier = 25,
                lateNightRateMultiplier = 25,
                baseShiftDurationHours = 8,
                shiftStartTime = "17:00",
                shiftEndTime = "02:00",
                lateNightStartTime = "22:00",
                lateNightEndTime = "05:00",
                lunchStartTime = "21:00",
                lunchDurationMinutes = 60,
                receiveNotifications = true,
                notificationTime = "14:00"
            )
        }
    }
}
