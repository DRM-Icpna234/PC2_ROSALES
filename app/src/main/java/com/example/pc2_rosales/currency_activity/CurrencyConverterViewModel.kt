package com.example.pc2_rosales.currency_activity

import android.util.Log
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
            .addOnFailureListener { exception ->
                Log.e("CurrencyViewModel", "Error al cargar monedas", exception)
                _currencies.value = emptyList()
            }
    }

    fun convert(amount: Double, from: String, to: String) {
        if (from == to) {
            _conversionResult.value = "Las monedas seleccionadas son iguales. No se requiere conversión."
            return
        }

        val rate = getMockRate(from, to)

        if (rate == null) {
            _conversionResult.value = "Tasa de conversión no disponible para $from -> $to"
            return
        }

        val converted = amount * rate
        _conversionResult.value = "%.2f %s equivalen a %.2f %s".format(amount, from, converted, to)
    }

    private fun getMockRate(from: String, to: String): Double? {
        return when (from to to) {
            "USD" to "EUR" -> 0.925
            "EUR" to "USD" -> 1.08
            "USD" to "PEN" -> 3.7
            "PEN" to "USD" -> 0.27
            "EUR" to "PEN" -> 3.98
            "PEN" to "EUR" -> 0.25
            else -> null // tasa no disponible
        }
    }

    fun resetResult() {
        _conversionResult.value = null
    }

    fun convertAndSave(
        amount: Double,
        from: String,
        to: String,
        userId: String
    ) {
        // 1) obtener tasa: rateTo / rateFrom de mainViewModel.currencyMap
        // 2) calcular resultado
        // 3) _conversionResult.value = "...equivalen a..."
        // 4) guardar en Firestore en "conversions" con serverTimestamp() y userId
    }
}