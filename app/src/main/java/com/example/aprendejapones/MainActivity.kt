package com.example.aprendejapones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.aprendejapones.presentation.navigation.KotodamaNavGraph
import com.example.aprendejapones.presentation.theme.KotodamaTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity - Entry point with Hilt
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotodamaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KotodamaNavGraph()
                }
            }
        }
    }
}