package com.example.aprendejapones.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aprendejapones.presentation.components.cards.*
import com.example.aprendejapones.presentation.theme.*

/**
 * Pantalla de Perfil
 */
@Composable
fun ProfileScreen(
    onNavigateToAchievements: () -> Unit = {},
    onNavigateToStats: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    // Manejar efectos
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ProfileEffect.NavigateToAchievements -> onNavigateToAchievements()
                is ProfileEffect.NavigateToStats -> onNavigateToStats()
                is ProfileEffect.ShowToast -> {
                    // TODO: Mostrar toast
                }
            }
        }
    }

    // Mostrar error
    state.error?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(ProfileEvent.DismissError) },
            title = { Text("Error") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(ProfileEvent.DismissError) }) {
                    Text("OK")
                }
            }
        )
    }

    ProfileContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun ProfileContent(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceWhite)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "👤 Tu Perfil",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Logros y Progreso",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Content
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            UserInfoCard(
                username = state.username,
                rank = state.rank,
                memberSince = state.memberSince,
                level = state.level,
                currentXP = state.currentXP,
                maxXP = state.maxXP,
                xpProgress = state.xpProgress
            )

            StatsCard(
                streak = state.stats.streak,
                lessonsCompleted = state.stats.lessonsCompleted,
                totalTime = state.stats.totalTimeHours,
                onClickStats = {
                    // Enviar evento de navegación a Stats
                    onEvent(ProfileEvent.NavigateToStats)
                }
            )

            AchievementsPreviewCard(
                achievements = state.achievements,
                unlockedCount = state.unlockedAchievementsCount,
                totalCount = state.totalAchievements,
                onClickAchievements = {
                    // Enviar evento de navegación a Achievements
                    onEvent(ProfileEvent.NavigateToAchievements)
                }
            )

            RecentActivityCard(
                activities = state.recentActivity
            )
        }
    }
}