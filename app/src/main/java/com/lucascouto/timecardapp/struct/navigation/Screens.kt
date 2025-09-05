package com.lucascouto.timecardapp.struct.navigation

sealed class Screens(val route: String) {
    object Splash : Screens("splash")
    object Home : Screens("home")
    object Profile : Screens("details")
    object ShowEntry : Screens("show_entry")
    // Add other screens as needed
}