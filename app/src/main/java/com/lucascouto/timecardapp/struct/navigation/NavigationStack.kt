package com.lucascouto.timecardapp.struct.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucascouto.timecardapp.ui.screens.HomeScreen
import com.lucascouto.timecardapp.ui.screens.SplashScreen

@Composable
fun NavigationStack() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        startDestination = Screens.Splash.route,
        navController = navController,
    ) {
        composable(Screens.Splash.route) { SplashScreen(navController) }
        composable(Screens.Home.route) { HomeScreen() }
        composable(Screens.Profile.route) { Text("Settings Screen") }
    }
}
