package com.example.aprendejapones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.aprendejapones.presentation.navigation.KotodamaNavGraph
import com.example.aprendejapones.presentation.navigation.Screen
import com.example.aprendejapones.presentation.screens.splash.SplashViewModel
import com.example.aprendejapones.presentation.theme.KotodamaTheme
import com.example.aprendejapones.presentation.theme.PrimaryGreen
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity - Entry point with Hilt
 * Usa SplashViewModel para determinar la pantalla inicial
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotodamaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Observar destino de navegación desde ViewModel
                    val destination by splashViewModel.navigationDestination.collectAsState()

                    when (destination) {
                        null -> {
                            // Cargando (determinando destino)
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
}