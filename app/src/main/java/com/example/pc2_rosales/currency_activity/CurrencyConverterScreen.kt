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
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.example.pc2_rosales.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

@Composable
fun CurrencyConverterScreen(
    userId: String,
    mainViewModel: MainViewModel = viewModel(),
    viewModel: CurrencyConverterViewModel = viewModel()
) {
    val context = LocalContext.current

    // Obtiene mapa de tasas y userId compartido
    val currencyMap by mainViewModel.currencyMap.collectAsState()
    val currencies = currencyMap.keys.toList()

    // Estado para el resultado (string formateado)
    val conversionResult by viewModel.conversionResult.collectAsState()

    var amount by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("") }
    var toCurrency by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Conversor de Divisas", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("De", style = MaterialTheme.typography.bodyLarge)
        CurrencyDropdown(
            label = "Moneda de origen",
            currencyList = currencies.filter { it != toCurrency },
            selectedCurrency = fromCurrency,
            onCurrencySelected = { fromCurrency = it; viewModel.resetResult() }
        )

        Icon(
            imageVector = Icons.Default.SwapVert,
            contentDescription = "Intercambiar",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    val tmp = fromCurrency
                    fromCurrency = toCurrency
                    toCurrency = tmp
                    viewModel.resetResult()
                }
        )

        Text("A", style = MaterialTheme.typography.bodyLarge)
        CurrencyDropdown(
            label = "Moneda de destino",
            currencyList = currencies.filter { it != fromCurrency },
            selectedCurrency = toCurrency,
            onCurrencySelected = { toCurrency = it; viewModel.resetResult() }
        )

        Button(
            onClick = {
                val amt = amount.toDoubleOrNull()
                if (amt != null && fromCurrency.isNotBlank() && toCurrency.isNotBlank()) {
                    viewModel.convertAndSave(amt, fromCurrency, toCurrency, userId)
                } else {
                    Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convertir")
        }

        conversionResult?.let { Text(it, style = MaterialTheme.typography.bodyLarge) }
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
                .clickable { expanded = true },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            currencyList.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(currency) },
                    onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}