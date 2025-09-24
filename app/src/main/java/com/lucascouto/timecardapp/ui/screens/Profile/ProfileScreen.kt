package com.lucascouto.timecardapp.ui.screens.Profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucascouto.timecardapp.struct.AppManager
import com.lucascouto.timecardapp.ui.layout.BasePage
import com.lucascouto.timecardapp.ui.screens.Profile.features.ProfileDestinations
import com.lucascouto.timecardapp.ui.screens.Profile.sub.ProfileContent
import com.lucascouto.timecardapp.ui.screens.Profile.sub.Settings.SettingsContent
import com.lucascouto.timecardapp.ui.viewmodel.factories.SettingsViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(appManager: AppManager, incomingNavController: NavController) {
    val tabs = remember { ProfileDestinations.entries }
    val startDestination = remember { ProfileDestinations.PROFILE }
    val selectedContent = rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    // nav
    val navController = rememberNavController()

    BasePage(navController = incomingNavController) {
        Column {
            PrimaryTabRow(selectedTabIndex = selectedContent.intValue) {
                tabs.forEachIndexed { index, screen ->
                    Tab(
                        text = { Text(screen.title) },
                        selected = selectedContent.intValue == index,
                        onClick = {
                            selectedContent.intValue = index
                            navController.navigate(screen.name) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }

            NavHost(
                navController = navController,
                startDestination = startDestination.name
            ) {
                composable(ProfileDestinations.PROFILE.name) {
                    ProfileContent()
                }
                composable(ProfileDestinations.SETTINGS.name) {
                    val owner = LocalViewModelStoreOwner.current

                    owner?.let {
                        SettingsContent(
                            viewModel = viewModel(
                                it,
                                "SettingsViewModel",
                                SettingsViewModelFactory(appManager.shared.dataStorageManager)
                            )
                        )
                    }
                }
                composable(ProfileDestinations.SYSTEM.name) {
                    Text("System settings")
                }
            }
        }
    }
}