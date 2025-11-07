package com.example.aprendejapones.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aprendejapones.presentation.theme.*

/**
 * Card que muestra un mensaje del mascota Kitsune
 */
@Composable
fun KitsuneMessageCard(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = AccentOrange.copy(alpha = 0.3f),
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = AccentOrangeLight,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar de Kitsune
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(2.dp, AccentOrange, RoundedCornerShape(20.dp))
                    .background(Color.White, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🦊", fontSize = 22.sp)
            }

            // Mensaje
            Column {
                Text(
                    text = "Kitsune-sensei",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentOrangeDark
                )
                Text(
                    text = message,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}