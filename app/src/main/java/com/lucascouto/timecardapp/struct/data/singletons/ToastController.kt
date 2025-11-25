package com.lucascouto.timecardapp.struct.data.singletons

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

enum class ToastType {
    INFO,
    SUCCESS,
    ERROR,
    WARNING
}

data class ToastEvent(
    val message: String,
    val type: ToastType = ToastType.INFO
)

object ToastController {
    private val _events = MutableSharedFlow<ToastEvent>()
    val events = _events

    suspend fun show(message: String, type: ToastType = ToastType.INFO) {
        _events.emit(ToastEvent(message, type))
    }
}
