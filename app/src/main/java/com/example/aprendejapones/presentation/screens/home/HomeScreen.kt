package com.example.aprendejapones.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aprendejapones.presentation.components.cards.*
import com.example.aprendejapones.presentation.components.common.StatBadge
import com.example.aprendejapones.presentation.theme.*
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Pantalla Principal (Home)
 * Entry point que maneja el ViewModel y los efectos
 */
@Composable
fun HomeScreen(
    onNavigateToLesson: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Manejar efectos secundarios (navegación, toasts, etc.)
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is HomeEffect.NavigateToLesson -> {
                    onNavigateToLesson(effect.functionName)
                }
                is HomeEffect.ShowToast -> {
                    // TODO: Mostrar toast
                }
            }
        }
    }

    // Mostrar error si existe
    state.error?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(HomeEvent.DismissError) },
            title = { Text("Error") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(HomeEvent.DismissError) }) {
                    Text("OK")
                }
            }
        )
    }

    HomeContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

/**
 * Contenido de la pantalla Home
 * Componente stateless que solo renderiza según el estado
 */
@Composable
private fun HomeContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(bottom = 80.dp),  // Keep bottom padding for bottom nav or other elements
        verticalArrangement = Arrangement.spacedBy(8.dp)  // Consistent spacing between items
    ) {
        item {
            HeaderSection(
                rank = state.userRank,
                streak = state.streak,
                drops = state.drops,
                hasNotifications = state.hasNotifications
            )
        }
        item { TreeSection() }
        state.kitsuneMessage?.let { kitsuneMsg ->
            item {
                KitsuneMessageCard(
                    message = kitsuneMsg.message,
                    modifier = Modifier.padding(horizontal = 16.dp)  // Removed vertical padding as LazyColumn handles spacing
                )
            }
        }
        item {
            FunctionsGrid(
                functions = state.lessonFunctions,
                onSelectFunction = { functionName ->
                    onEvent(HomeEvent.SelectFunction(functionName))
                }
            )
        }
        state.dailyChallenge?.let { challenge ->
            item {
                DailyChallengeCard(
                    completed = challenge.completed,
                    total = challenge.total,
                    timeRemaining = challenge.timeRemaining,
                    rewardXP = challenge.rewardXP,
                    modifier = Modifier.padding(horizontal = 16.dp)  // Removed vertical padding
                )
            }
        }
    }
}

/**
 * Header con información del usuario
 */
@Composable
private fun HeaderSection(
    rank: String,
    streak: Int,
    drops: Int,
    hasNotifications: Boolean
) {
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
            // Avatar y rango
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(PrimaryGreen, RoundedCornerShape(24.dp))
                        .border(2.dp, PrimaryGreenDark, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "K",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = "Rango",
                        fontSize = 9.sp,
                        color = TextTertiary
                    )
                    Text(
                        text = rank,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
            // Stats y notificaciones
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatBadge(
                    text = "🔥 $streak",
                    accentColor = AccentRed
                )
                StatBadge(
                    text = "💧 $drops",
                    accentColor = AccentBlue
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .border(2.dp, BorderGray, RoundedCornerShape(18.dp))
                        .background(SurfaceGray, RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔔", fontSize = 16.sp)
                }
            }
        }
    }
}

/**
 * Sección del árbol (Santuario Digital)
 */
@Composable
private fun TreeSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(2.dp, BorderGray, RoundedCornerShape(12.dp))
            .background(SurfaceWhite, RoundedCornerShape(12.dp))
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🌳",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = "Tu Santuario Digital",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "こんにちは！",
                fontSize = 14.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * Grid de funciones disponibles
 */
@Composable
private fun FunctionsGrid(
    functions: List<com.example.aprendejapones.domain.model.LessonFunction>,
    onSelectFunction: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Funciones",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 14.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            for (i in functions.indices step 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        FunctionCard(
                            icon = functions[i].icon,
                            name = functions[i].name,
                            subtitle = functions[i].subtitle,
                            isLocked = functions[i].isLocked,
                            onClick = { onSelectFunction(functions[i].name) }
                        )
                    }
                    if (i + 1 < functions.size) {
                        Box(modifier = Modifier.weight(1f)) {
                            FunctionCard(
                                icon = functions[i + 1].icon,
                                name = functions[i + 1].name,
                                subtitle = functions[i + 1].subtitle,
                                isLocked = functions[i + 1].isLocked,
                                onClick = { onSelectFunction(functions[i + 1].name) }
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}