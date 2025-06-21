package com.example.pc2_rosales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.compose.rememberNavController
import com.example.pc2_rosales.login_activity.LoginScreen
import com.example.pc2_rosales.ui.theme.Pc2RosalesTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase (es seguro llamarlo incluso si ya fue inicializado automáticamente)
        FirebaseApp.initializeApp(this)

        setContent {
            Pc2RosalesTheme {
                val navController = rememberNavController()
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation(navController)
                }
            }
        }
    }
}