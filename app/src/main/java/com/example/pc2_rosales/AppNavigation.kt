package com.example.pc2_rosales

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pc2_rosales.currency_activity.CurrencyConverterScreen
import com.example.pc2_rosales.login_activity.LoginScreen
import androidx.compose.runtime.getValue

object Routes {
    const val LOGIN = "login"
    const val CURRENCY = "currency"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val mainViewModel: MainViewModel = viewModel()
    val currencyMap by mainViewModel.currencyMap.collectAsState()
    val userId by mainViewModel.userId.collectAsState()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
        composable(Routes.CURRENCY) {
            CurrencyConverterScreen(userId = userId)
        }
    }
}