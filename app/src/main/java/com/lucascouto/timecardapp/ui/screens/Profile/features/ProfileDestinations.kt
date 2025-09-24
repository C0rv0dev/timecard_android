package com.lucascouto.timecardapp.ui.screens.Profile.features

data class ProfileDestination(
    val route: String,
    val title: String,
)

enum class ProfileDestinations(val destination: ProfileDestination) {
    PROFILE(ProfileDestination("profile_profile", "Profile")),
    SETTINGS(ProfileDestination("profile_settings", "Settings")),
    SYSTEM(ProfileDestination("profile_system", "System"));

    val title: String
        get() = destination.title
}
