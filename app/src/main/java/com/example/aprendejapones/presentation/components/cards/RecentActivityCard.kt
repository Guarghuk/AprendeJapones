package com.example.aprendejapones.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aprendejapones.utils.Activity
import com.example.aprendejapones.presentation.theme.*

/**
 * Card que muestra la actividad reciente del usuario
 * Lista las últimas acciones realizadas con timestamp
 */
@Composable
fun RecentActivityCard(
    activities: List<Activity>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(SurfaceWhite, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Actividad Reciente",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                activities.forEach { activity ->
                    ActivityItemRow(
                        description = activity.description,
                        timeAgo = activity.time
                    )
                }
            }
        }
    }
}

/**
 * Fila individual de actividad
 */
@Composable
private fun ActivityItemRow(
    description: String,
    timeAgo: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = description,
            fontSize = 12.sp,
            color = TextPrimary
        )
        Text(
            text = timeAgo,
            fontSize = 10.sp,
            color = TextTertiary,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
