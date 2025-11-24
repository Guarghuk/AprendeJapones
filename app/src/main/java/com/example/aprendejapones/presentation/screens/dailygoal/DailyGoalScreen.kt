package com.example.aprendejapones.presentation.screens.dailygoal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aprendejapones.presentation.theme.*

@Composable
fun DailyGoalScreen(
    onBack: () -> Unit,
    viewModel: DailyGoalViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is DailyGoalEffect.ShowToast -> {
                    // TODO: Implement toast
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        // Header
        DailyGoalHeader(onBack = onBack)

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current goal display
            CurrentGoalCard(
                minutes = state.selectedGoal,
                daysCompleted = state.daysCompleted,
                currentStreak = state.currentStreak
            )

            // Goal options
            Text(
                text = "Selecciona tu meta diaria",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            GoalOption(
                minutes = 5,
                label = "Casual",
                description = "Perfecto para comenzar",
                isSelected = state.selectedGoal == 5,
                onClick = { viewModel.onEvent(DailyGoalEvent.SelectGoal(5)) }
            )

            GoalOption(
                minutes = 10,
                label = "Regular",
                description = "Buen balance",
                isSelected = state.selectedGoal == 10,
                onClick = { viewModel.onEvent(DailyGoalEvent.SelectGoal(10)) }
            )

            GoalOption(
                minutes = 15,
                label = "Serio",
                description = "Progreso constante",
                isSelected = state.selectedGoal == 15,
                onClick = { viewModel.onEvent(DailyGoalEvent.SelectGoal(15)) }
            )

            GoalOption(
                minutes = 20,
                label = "Intenso",
                description = "Avance rápido",
                isSelected = state.selectedGoal == 20,
                onClick = { viewModel.onEvent(DailyGoalEvent.SelectGoal(20)) }
            )

            GoalOption(
                minutes = 30,
                label = "Extremo",
                description = "Máxima dedicación",
                isSelected = state.selectedGoal == 30,
                onClick = { viewModel.onEvent(DailyGoalEvent.SelectGoal(30)) }
            )

            // Save button
            Button(
                onClick = { viewModel.onEvent(DailyGoalEvent.SaveGoal) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(2.dp, PrimaryGreen, RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "Guardar Meta",
                    color = SurfaceWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Info card
            InfoCard()
        }
    }
}

@Composable
private fun DailyGoalHeader(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                text = "🎯 Meta Diaria",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.width(60.dp))
        }
    }
}

@Composable
private fun CurrentGoalCard(
    minutes: Int,
    daysCompleted: Int,
    currentStreak: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, PrimaryGreen, RoundedCornerShape(12.dp))
            .background(PrimaryGreenLight, RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "⏱️",
                fontSize = 48.sp
            )

            Text(
                text = "$minutes minutos",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "Meta actual",
                fontSize = 12.sp,
                color = PrimaryGreenDark
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = PrimaryGreen.copy(alpha = 0.3f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("$daysCompleted", "Días completados")
                StatItem("$currentStreak", "Racha actual")
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GoalOption(
    minutes: Int,
    label: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) PrimaryGreen else BorderGray
    val backgroundColor = if (isSelected) PrimaryGreenLight else SurfaceWhite

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$minutes minutos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "$label - $description",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(2.dp, PrimaryGreen, RoundedCornerShape(12.dp))
                        .background(PrimaryGreen, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", color = SurfaceWhite, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun InfoCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, AccentBlue.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .background(AccentBlue.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💡", fontSize = 20.sp)
                Text(
                    text = "Consejo",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(
                text = "Establece una meta realista y auméntala gradualmente. La consistencia es más importante que la duración.",
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}