package com.example.pc2_rosales

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val _currencyMap = MutableStateFlow<Map<String, Double>>(emptyMap())
    val currencyMap: StateFlow<Map<String, Double>> = _currencyMap

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    fun setCurrencyMap(map: Map<String, Double>) {
        _currencyMap.value = map
    }

    fun setUserId(id: String) {
        _userId.value = id
    }
}