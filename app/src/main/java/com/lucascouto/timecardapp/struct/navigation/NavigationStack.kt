package com.lucascouto.timecardapp.struct.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucascouto.timecardapp.struct.AppManager
import com.lucascouto.timecardapp.ui.screens.Home.HomeScreen
import com.lucascouto.timecardapp.ui.screens.Home.HomeViewModel
import com.lucascouto.timecardapp.ui.screens.Splash.SplashScreen

@Composable
fun NavigationStack(appManager: AppManager) {
    val navController: NavHostController = rememberNavController()

    // Define viewModels here if needed and pass them to screens
    val homeViewModel = remember { HomeViewModel() }

    NavHost(
        startDestination = if (appManager.shared.isInDebugMode) Screens.Home.route else Screens.Splash.route,
        navController = navController,
        enterTransition = { slideInHorizontally(animationSpec = tween(1000)) },
        exitTransition = { slideOutHorizontally(animationSpec = tween(1000)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -600 }, animationSpec = tween(1000)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 600 }, animationSpec = tween(1000)) }
    ) {
        composable(Screens.Splash.route) { backStackEntry -> SplashScreen(navController) }
        composable(route = Screens.Home.route) { backStackEntry -> HomeScreen(homeViewModel, navController) }
        composable(Screens.Profile.route) { backStackEntry -> Text("Settings Screen") }
    }
}
