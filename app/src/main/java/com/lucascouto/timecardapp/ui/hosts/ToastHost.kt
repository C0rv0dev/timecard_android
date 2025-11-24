package com.lucascouto.timecardapp.ui.hosts

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.lucascouto.timecardapp.struct.data.singletons.ToastController
import com.lucascouto.timecardapp.struct.data.singletons.ToastEvent
import com.lucascouto.timecardapp.struct.data.singletons.ToastType

@Composable
fun ToastHost(content: @Composable () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    var lastToastEvent by remember { mutableStateOf<ToastEvent?>(null) }

    LaunchedEffect(Unit) {
        ToastController.events.collect { event ->
            lastToastEvent = event
            snackbarHostState.showSnackbar(message = event.message)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackBarData ->
                Box(Modifier.padding(16.dp)) {
                    CustomSnackbar(
                        message = snackBarData.visuals.message,
                        type = lastToastEvent?.type ?: ToastType.INFO
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            content()
        }
    }
}

@Composable
fun CustomSnackbar(
    message: String,
    type: ToastType = ToastType.INFO,
    isRtl: Boolean = true,
    @DrawableRes drawableRes: Int? = null,
) {
    fun getContainerColorByType(type: ToastType): Color {
        return when (type) {
            ToastType.INFO -> Color(0xFF323232)
            ToastType.SUCCESS -> Color(0xFF4CAF50)
            ToastType.ERROR -> Color(0xFFF44336)
            ToastType.WARNING -> Color(0xFFFFC107)
        }
    }

    Snackbar(containerColor = getContainerColorByType(type)) {
        CompositionLocalProvider(
            LocalLayoutDirection provides
                    if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
        ) {
            Row {
                if (drawableRes != null) {
                    Icon(
                        painterResource(id = drawableRes),
                        contentDescription = null
                    )
                }

                Text(message)
            }
        }
    }
}
