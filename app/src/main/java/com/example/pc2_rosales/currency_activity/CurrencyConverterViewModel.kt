package com.example.pc2_rosales.currency_activity

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CurrencyConverterViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _currencies = MutableStateFlow<List<String>>(emptyList())
    val currencies: StateFlow<List<String>> = _currencies

    private val _conversionResult = MutableStateFlow<String?>(null)
    val conversionResult: StateFlow<String?> = _conversionResult

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        db.collection("currency").get()
            .addOnSuccessListener { result ->
                val currencyList = result.mapNotNull { it.getString("abr") }
                _currencies.value = currencyList
            }
    }

    fun convert(amount: Double, from: String, to: String) {
        // Este valor es simulado. En una app real deberías consultar una API externa o Firestore.
        val rate = getMockRate(from, to)
        val converted = amount * rate
        _conversionResult.value = "%.2f %s equivalen a %.2f %s".format(amount, from, converted, to)
    }

    private fun getMockRate(from: String, to: String): Double {
        // Simulación de tasas de cambio
        return when (from to to) {
            "USD" to "EUR" -> 0.925
            "EUR" to "USD" -> 1.08
            "USD" to "PEN" -> 3.7
            "PEN" to "USD" -> 0.27
            else -> 1.0 // tasa por defecto
        }
    }

    fun resetResult() {
        _conversionResult.value = null
    }
}