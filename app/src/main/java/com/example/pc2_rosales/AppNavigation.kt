package com.example.pc2_rosales

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pc2_rosales.currency_activity.CurrencyConverterScreen
import com.example.pc2_rosales.login_activity.LoginScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("currency") { CurrencyConverterScreen() }
    }
}