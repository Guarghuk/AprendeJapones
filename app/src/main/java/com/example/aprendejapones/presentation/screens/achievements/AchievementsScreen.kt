package com.example.aprendejapones.presentation.screens.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aprendejapones.presentation.theme.*
import com.example.aprendejapones.utils.Achievement

@Composable
fun AchievementsScreen(
    onBack: () -> Unit,
    viewModel: AchievementsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        // Header
        AchievementsHeader(
            unlockedCount = state.unlockedCount,
            totalCount = state.totalCount,
            onBack = onBack
        )

        // Content
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(state.achievements) { achievement ->
                    AchievementCard(achievement = achievement)
                }
            }
        }
    }
}

@Composable
private fun AchievementsHeader(
    unlockedCount: Int,
    totalCount: Int,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
    ) {
        // Title bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text(
                    text = "← Volver",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "🏆 Logros",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.width(60.dp))
        }

        // Progress summary
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .border(2.dp, PrimaryGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .background(PrimaryGreenLight, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$unlockedCount/$totalCount",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
                Text(
                    text = "Logros Desbloqueados",
                    fontSize = 12.sp,
                    color = PrimaryGreenDark,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun AchievementCard(achievement: Achievement) {
    val containerColor = if (achievement.isUnlocked) SurfaceWhite else SurfaceGray.copy(alpha = 0.5f)
    val iconAlpha = if (achievement.isUnlocked) 1f else 0.3f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(containerColor, RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .border(
                        2.dp,
                        if (achievement.isUnlocked) PrimaryGreen else BorderGray,
                        RoundedCornerShape(30.dp)
                    )
                    .background(
                        if (achievement.isUnlocked) PrimaryGreenLight else BackgroundGray,
                        RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = achievement.icon,
                    fontSize = 32.sp,
                    modifier = Modifier.alpha(iconAlpha)
                )
            }

            // Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = achievement.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (achievement.isUnlocked) TextPrimary else TextTertiary
                )
                Text(
                    text = achievement.description,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                if (achievement.isUnlocked) {
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .border(1.dp, SuccessGreen, RoundedCornerShape(4.dp))
                            .background(SuccessGreen.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "✓ Desbloqueado",
                            fontSize = 9.sp,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .border(1.dp, TextTertiary, RoundedCornerShape(4.dp))
                            .background(BackgroundGray, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "🔒 Bloqueado",
                            fontSize = 9.sp,
                            color = TextTertiary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}