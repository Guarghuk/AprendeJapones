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
import com.example.aprendejapones.utils.Achievement
import com.example.aprendejapones.presentation.theme.*

/**
 * Card que muestra una vista previa de los logros desbloqueados
 * Muestra los primeros 8 logros en una grid
 */
@Composable
fun AchievementsPreviewCard(
    achievements: List<Achievement>,
    unlockedCount: Int,
    totalCount: Int,
    onClickAchievements: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(SurfaceWhite, RoundedCornerShape(10.dp))
            .clickable { onClickAchievements() }
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
                    text = "Logros Desbloqueados",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "$unlockedCount/$totalCount →",
                    fontSize = 12.sp,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold
                )
            }

            // Grid de logros (máximo 8)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Tomar solo los primeros 8 logros desbloqueados
                val displayedAchievements = achievements.filter { it.isUnlocked }.take(8)

                displayedAchievements.forEach { achievement ->
                    AchievementBadge(
                        icon = achievement.icon,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }

                // Rellenar con espacios vacíos si hay menos de 8
                repeat(maxOf(0, 8 - displayedAchievements.size)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }
            }
        }
    }
}

/**
 * Badge individual para cada logro
 */
@Composable
private fun AchievementBadge(
    icon: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = 2.dp,
                color = PrimaryGreen.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = PrimaryGreenLight,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = 18.sp
        )
    }
}

