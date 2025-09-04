package com.lucascouto.timecardapp.ui.screens.Profile.features

data class ProfileDestination(
    val route: String,
    val title: String,
)

enum class ProfileDestinations(val destination: ProfileDestination) {
    PROFILE(ProfileDestination("profile_profile", "Profile")),
    SETTINGS(ProfileDestination("profile_settings", "Settings"));

    val title: String
        get() = destination.title

    val route: String
        get() = destination.route
}
