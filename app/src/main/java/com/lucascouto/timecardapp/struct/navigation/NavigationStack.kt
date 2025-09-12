package com.lucascouto.timecardapp.struct.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lucascouto.timecardapp.struct.AppManager
import com.lucascouto.timecardapp.ui.screens.Entry.Add.AddEntryScreen
import com.lucascouto.timecardapp.ui.screens.Entry.Show.ShowEntryScreen
import com.lucascouto.timecardapp.ui.screens.Entry.EntryViewModel
import com.lucascouto.timecardapp.ui.screens.Home.HomeScreen
import com.lucascouto.timecardapp.ui.screens.Home.HomeViewModel
import com.lucascouto.timecardapp.ui.screens.Profile.ProfileScreen
import com.lucascouto.timecardapp.ui.screens.Splash.SplashScreen
import java.time.LocalDate


@Composable
fun NavigationStack(appManager: AppManager) {
    val navController: NavHostController = rememberNavController()

    // Define viewModels here if needed and pass them to screens
    val homeViewModel = remember { HomeViewModel() }
    val entryViewModel = remember { EntryViewModel() }

    NavHost(
        startDestination = if (appManager.shared.isInDebugMode) Screens.Home.route else Screens.Splash.route,
        navController = navController,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Companion.Left, tween(500)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)) }
    ) {
        // Main
        composable(Screens.Splash.route) { backStackEntry -> SplashScreen(navController) }
        composable(route = Screens.Home.route) { backStackEntry -> HomeScreen(homeViewModel, navController) }
        composable(Screens.Profile.route) { backStackEntry -> ProfileScreen(navController) }

        // Entries
        // Show
        composable(
            Screens.ShowEntry.route + "/{date}",
            arguments = listOf(
                navArgument("date") {
                    type = NavType.StringType
                    defaultValue = LocalDate.now().toString()
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()
            ShowEntryScreen(entryViewModel, navController, date)
        }

        // Add
        composable(
            Screens.AddEntry.route + "/{date}",
            arguments = listOf(
                navArgument("date") {
                    type = NavType.StringType
                    defaultValue = LocalDate.now().toString()
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()
            AddEntryScreen(entryViewModel, navController)
        }
    }
}
