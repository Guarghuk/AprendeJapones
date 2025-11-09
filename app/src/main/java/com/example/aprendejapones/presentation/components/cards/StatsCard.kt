package com.example.aprendejapones.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aprendejapones.presentation.theme.*

/**
 * Card que muestra estadísticas generales del usuario
 * Incluye racha, lecciones completadas y tiempo total de estudio
 */
@Composable
fun StatsCard(
    streak: Int,
    lessonsCompleted: Int,
    totalTime: String,
    onClickStats: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(SurfaceWhite, RoundedCornerShape(10.dp))
            .clickable { onClickStats() }
            .padding(16.dp)
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estadísticas Generales",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "→",
                    fontSize = 16.sp,
                    color = PrimaryGreen
                )
            }

            // Stats Grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Racha
                StatsItem(
                    icon = "🔥",
                    value = streak.toString(),
                    label = "Racha",
                    modifier = Modifier.weight(1f)
                )

                // Lecciones
                StatsItem(
                    icon = "📚",
                    value = lessonsCompleted.toString(),
                    label = "Lecciones",
                    modifier = Modifier.weight(1f)
                )

                // Tiempo
                StatsItem(
                    icon = "⏱️",
                    value = totalTime,
                    label = "Tiempo",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Componente interno para cada item de estadística
 */
@Composable
private fun StatsItem(
    icon: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 28.sp
        )
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
