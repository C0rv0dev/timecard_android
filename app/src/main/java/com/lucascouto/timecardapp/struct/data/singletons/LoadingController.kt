package com.lucascouto.timecardapp.struct.data.singletons

import kotlinx.coroutines.flow.MutableStateFlow

object LoadingController {
    private val _loading = MutableStateFlow(false)
    val loading = _loading


    fun setLoading(set: Boolean) {
        loading.value = set
    }
}
