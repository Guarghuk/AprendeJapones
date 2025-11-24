package com.example.aprendejapones.presentation.screens.onboarding.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aprendejapones.presentation.theme.*

/**
 * Primera página: Bienvenida
 */
@Composable
fun WelcomePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo/Emoji grande
        Box(
            modifier = Modifier
                .size(140.dp)
                .border(4.dp, PrimaryGreen, RoundedCornerShape(70.dp))
                .background(PrimaryGreenLight, RoundedCornerShape(70.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("🌸", fontSize = 80.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Título
        Text(
            text = "Bienvenido a",
            fontSize = 18.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Kotodama",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "言霊",
            fontSize = 28.sp,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Descripción
        Text(
            text = "Tu compañero para dominar el japonés de forma divertida y efectiva",
            fontSize = 16.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}