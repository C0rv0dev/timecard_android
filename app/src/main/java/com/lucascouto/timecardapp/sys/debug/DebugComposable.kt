package com.lucascouto.timecardapp.sys.debug

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember

@Composable
fun DebugComposable(tag: String) {
    val count = remember { mutableIntStateOf(0) }
    SideEffect { count.intValue++ }
    Text("$tag recomposed ${count.intValue} times")
}
