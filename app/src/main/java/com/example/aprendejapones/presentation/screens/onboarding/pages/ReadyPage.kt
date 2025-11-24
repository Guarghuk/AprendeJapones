package com.example.aprendejapones.presentation.screens.onboarding.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
 * Tercera página: Configuración de perfil
 */
@Composable
fun ProfileSetupPage(
    userName: String,
    onUserNameChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .border(3.dp, PrimaryGreen, RoundedCornerShape(50.dp))
                .background(PrimaryGreenLight, RoundedCornerShape(50.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (userName.isNotEmpty()) userName.first().uppercase() else "?",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Título
        Text(
            text = "¡Último paso!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Text(
            text = "¿Cómo te llamas?",
            fontSize = 18.sp,
            color = TextSecondary,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Input de nombre
        OutlinedTextField(
            value = userName,
            onValueChange = onUserNameChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Tu nombre", color = TextTertiary, fontSize = 14.sp)
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = BorderGray,
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción
        Text(
            text = "Este nombre aparecerá en tu perfil y en la comunidad",
            fontSize = 12.sp,
            color = TextTertiary,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}