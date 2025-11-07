package com.example.aprendejapones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.aprendejapones.presentation.screens.home.HomeScreen
import com.example.aprendejapones.presentation.theme.KotodamaTheme

/**
 * MainActivity refactorizada
 * Ahora usa el tema y estructura limpia
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotodamaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Por ahora solo HomeScreen, luego agregaremos Navigation
                    HomeScreen(
                        onNavigateToLesson = { functionName ->
                            // TODO: Navegar a LessonScreen cuando esté migrado
                            println("Navegando a: $functionName")
                        }
                    )
                }
            }
        }
    }
}