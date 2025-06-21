package com.example.pc2_rosales.currency_activity

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

@Composable
fun CurrencyConverterScreen(
    currencyMap: Map<String, Double>,
    userId: String
) {
    val context = LocalContext.current

    var amount by remember { mutableStateOf("") }
    var convertedAmount by remember { mutableStateOf("") }
    var selectedOriginCurrencyId by remember { mutableStateOf("") }
    var selectedDestinationCurrencyId by remember { mutableStateOf("") }

    val currencyList = currencyMap.keys.toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Conversor de divisas", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth()
        )

        // Selector de moneda de origen
        CurrencyDropdown(
            label = "Moneda de origen",
            currencyList = currencyList.filter { it != selectedDestinationCurrencyId },
            selectedCurrency = selectedOriginCurrencyId,
            onCurrencySelected = { selectedOriginCurrencyId = it }
        )

        // Selector de moneda de destino
        CurrencyDropdown(
            label = "Moneda de destino",
            currencyList = currencyList.filter { it != selectedOriginCurrencyId },
            selectedCurrency = selectedDestinationCurrencyId,
            onCurrencySelected = { selectedDestinationCurrencyId = it }
        )

        // Botón de conversión
        Button(
            onClick = {
                val originRate = currencyMap[selectedOriginCurrencyId]
                val destRate = currencyMap[selectedDestinationCurrencyId]
                val amountValue = amount.toDoubleOrNull()

                if (originRate != null && destRate != null && amountValue != null) {
                    val result = (amountValue / originRate) * destRate
                    convertedAmount = "%.2f".format(result)

                    // Guardar en Firebase
                    val firestore = Firebase.firestore

                    val data = hashMapOf(
                        "amount" to amountValue,
                        "result" to result,
                        "originCurrency" to selectedOriginCurrencyId,
                        "destinyCurrency" to selectedDestinationCurrencyId,
                        "date" to FieldValue.serverTimestamp(),
                        "user" to userId
                    )

                    firestore.collection("conversions")
                        .add(data)
                        .addOnSuccessListener {
                            Toast
                                .makeText(context, "Conversión guardada en Firebase", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .addOnFailureListener {
                            Toast
                                .makeText(context, "Error al guardar conversión", Toast.LENGTH_SHORT)
                                .show()
                        }
                } else {
                    Toast
                        .makeText(context, "Verifica que todos los datos estén completos", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convertir")
        }

        if (convertedAmount.isNotEmpty()) {
            Text("Resultado: $convertedAmount", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun CurrencyDropdown(
    label: String,
    currencyList: List<String>,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedCurrency,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencyList.forEach { currencyId ->
                DropdownMenuItem(
                    text = { Text(currencyId) },
                    onClick = {
                        onCurrencySelected(currencyId)
                        expanded = false
                    }
                )
            }
        }
    }
}