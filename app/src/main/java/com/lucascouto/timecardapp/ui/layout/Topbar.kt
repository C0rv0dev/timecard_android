package com.lucascouto.timecardapp.ui.layout

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lucascouto.timecardapp.R
import com.lucascouto.timecardapp.struct.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    var icon = painterResource(R.drawable.ic_arrow_back)
    if (navController.currentDestination?.route !== Screens.Profile.route) icon =
        painterResource(R.drawable.ic_person)

    TopAppBar(
        title = {},
        actions = {
            ElevatedButton(
                onClick = { handleNavigate(navController) },
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    painter = icon,
                    contentDescription = "Profile"
                )
            }
        }
    )
}

fun handleNavigate(navController: NavController) {
    if (navController.currentDestination!!.route !== Screens.Profile.route) {
        navController.navigate(Screens.Profile.route) {
            launchSingleTop = true
            restoreState = true
        }
    } else {
        navController.popBackStack()
    }
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(rememberNavController())
}
