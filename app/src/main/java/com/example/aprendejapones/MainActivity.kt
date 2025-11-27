package com.example.aprendejapones

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.aprendejapones.presentation.navigation.KotodamaNavGraph
import com.example.aprendejapones.presentation.navigation.Screen
import com.example.aprendejapones.presentation.screens.splash.SplashViewModel
import com.example.aprendejapones.presentation.theme.KotodamaTheme
import com.example.aprendejapones.presentation.theme.PrimaryGreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    // ✅ Agregar launcher para permisos
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Permiso concedido o denegado
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Solicitar permiso de notificaciones (Android 13+)
        requestNotificationPermission()

        setContent {
            KotodamaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val destination by splashViewModel.navigationDestination.collectAsState()

                    when (destination) {
                        null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = PrimaryGreen)
                            }
                        }
                        "onboarding" -> {
                            KotodamaNavGraph(startDestination = Screen.Onboarding.route)
                        }
                        "home" -> {
                            KotodamaNavGraph(startDestination = Screen.Home.route)
                        }
                    }
                }
            }
        }
    }

    // ✅ Nueva función para solicitar permisos
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}