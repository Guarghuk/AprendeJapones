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
 * Tercera página: ¡Listo para comenzar!
 * Muestra un mensaje de bienvenida personalizado con el nombre del usuario
 */
@Composable
fun ReadyPage(
    userName: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Avatar con inicial del usuario
        Box(
            modifier = Modifier
                .size(120.dp)
                .border(4.dp, PrimaryGreen, RoundedCornerShape(60.dp))
                .background(PrimaryGreenLight, RoundedCornerShape(60.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (userName.isNotEmpty()) userName.first().uppercase() else "🎉",
                fontSize = if (userName.isNotEmpty()) 56.sp else 64.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Saludo personalizado
        Text(
            text = if (userName.isNotEmpty()) "¡Hola, $userName!" else "¡Todo listo!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de bienvenida
        Text(
            text = "Tu aventura para aprender japonés comienza ahora",
            fontSize = 18.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tips o recordatorios
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            TipItem(
                icon = "🎯",
                text = "Practica un poco cada día para mejores resultados"
            )
            TipItem(
                icon = "🏆",
                text = "Completa desafíos para ganar logros"
            )
            TipItem(
                icon = "💪",
                text = "No te rindas, ¡tú puedes!"
            )
        }
    }
}

@Composable
private fun TipItem(
    icon: String,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
            .background(SurfaceWhite, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 24.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = TextPrimary,
            lineHeight = 20.sp
        )
    }
}