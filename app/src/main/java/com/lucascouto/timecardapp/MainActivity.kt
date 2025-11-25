package com.lucascouto.timecardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.lucascouto.timecardapp.struct.navigation.NavigationStack
import com.lucascouto.timecardapp.struct.AppManager
import com.lucascouto.timecardapp.ui.hosts.LoaderHost
import com.lucascouto.timecardapp.ui.hosts.ToastHost
import com.lucascouto.timecardapp.ui.theme.TimecardAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the system bars and make the app full screen
        enableEdgeToEdge()
        setContent {
            // Initialize the AppManager
            val appManager = AppManager(LocalContext.current)
            appManager.shared.boot()

            TimecardAppTheme {
                ToastHost {
                    LoaderHost {
                        NavigationStack(appManager)
                    }
                }
            }
        }
    }
}
