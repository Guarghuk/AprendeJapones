package com.example.aprendejapones.presentation.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aprendejapones.presentation.theme.*

@Composable
fun StatsScreen(
    onBack: () -> Unit,
    viewModel: StatsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        // Header
        StatsHeader(onBack = onBack)

        // Content
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Overview card
                OverviewCard(
                    totalXP = state.totalXP,
                    currentLevel = state.currentLevel,
                    rank = state.rank
                )

                // Learning stats
                LearningStatsCard(
                    lessonsCompleted = state.lessonsCompleted,
                    studyTimeMinutes = state.studyTimeMinutes,
                    wordsLearned = state.wordsLearned,
                    kanjiLearned = state.kanjiLearned
                )

                // Streak stats
                StreakStatsCard(
                    currentStreak = state.currentStreak,
                    longestStreak = state.longestStreak,
                    totalDaysStudied = state.totalDaysStudied
                )

                // Practice breakdown
                PracticeBreakdownCard(
                    hiraganaProgress = state.hiraganaProgress,
                    katakanaProgress = state.katakanaProgress,
                    kanjiProgress = state.kanjiProgress,
                    grammarProgress = state.grammarProgress,
                    vocabularyProgress = state.vocabularyProgress
                )

                // Weekly activity
                WeeklyActivityCard(weeklyMinutes = state.weeklyMinutes)
            }
        }
    }
}

@Composable
private fun StatsHeader(onBack: () -> Unit) {
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
                text = "📊 Estadísticas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.width(60.dp))
        }
    }
}

@Composable
private fun OverviewCard(
    totalXP: Int,
    currentLevel: Int,
    rank: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, PrimaryGreen, RoundedCornerShape(12.dp))
            .background(PrimaryGreenLight, RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatColumn("🏆", "$currentLevel", "Nivel")
            VerticalDivider(modifier = Modifier.height(60.dp), color = PrimaryGreen.copy(alpha = 0.3f))
            StatColumn("✨", "$totalXP", "XP Total")
            VerticalDivider(modifier = Modifier.height(60.dp), color = PrimaryGreen.copy(alpha = 0.3f))
            StatColumn("👤", rank, "Rango")
        }
    }
}

@Composable
private fun StatColumn(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 28.sp)
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            modifier = Modifier.padding(top = 4.dp)
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
private fun LearningStatsCard(
    lessonsCompleted: Int,
    studyTimeMinutes: Int,
    wordsLearned: Int,
    kanjiLearned: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(SurfaceWhite, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Aprendizaje",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SmallStatCard("📚", "$lessonsCompleted", "Lecciones", Modifier.weight(1f))
                SmallStatCard("⏱️", "${studyTimeMinutes}m", "Tiempo", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SmallStatCard("💬", "$wordsLearned", "Palabras", Modifier.weight(1f))
                SmallStatCard("漢", "$kanjiLearned", "Kanji", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SmallStatCard(
    icon: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(1.dp, BorderLight, RoundedCornerShape(8.dp))
            .background(BackgroundGray, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(icon, fontSize = 24.sp)
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = label,
                fontSize = 9.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun StreakStatsCard(
    currentStreak: Int,
    longestStreak: Int,
    totalDaysStudied: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, AccentRed.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .background(AccentRed.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔥", fontSize = 20.sp)
                Text(
                    text = "Racha de Estudio",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StreakStatItem("$currentStreak", "Racha Actual")
                VerticalDivider(modifier = Modifier.height(50.dp))
                StreakStatItem("$longestStreak", "Mejor Racha")
                VerticalDivider(modifier = Modifier.height(50.dp))
                StreakStatItem("$totalDaysStudied", "Días Totales")
            }
        }
    }
}

@Composable
private fun StreakStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AccentRed
        )
        Text(
            text = label,
            fontSize = 9.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PracticeBreakdownCard(
    hiraganaProgress: Int,
    katakanaProgress: Int,
    kanjiProgress: Int,
    grammarProgress: Int,
    vocabularyProgress: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(SurfaceWhite, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Desglose de Práctica",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ProgressItem("あ Hiragana", hiraganaProgress)
            Spacer(modifier = Modifier.height(10.dp))
            ProgressItem("ア Katakana", katakanaProgress)
            Spacer(modifier = Modifier.height(10.dp))
            ProgressItem("漢 Kanji", kanjiProgress)
            Spacer(modifier = Modifier.height(10.dp))
            ProgressItem("📖 Gramática", grammarProgress)
            Spacer(modifier = Modifier.height(10.dp))
            ProgressItem("💬 Vocabulario", vocabularyProgress)
        }
    }
}

@Composable
private fun ProgressItem(label: String, progress: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextPrimary
            )
            Text(
                text = "$progress%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(8.dp)
                .border(1.dp, BorderGray, RoundedCornerShape(4.dp))
                .background(BackgroundGray, RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress / 100f)
                    .fillMaxHeight()
                    .background(PrimaryGreen, RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
private fun WeeklyActivityCard(weeklyMinutes: List<Int>) {
    val days = listOf("L", "M", "X", "J", "V", "S", "D")
    val maxMinutes = weeklyMinutes.maxOrNull() ?: 1

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(SurfaceWhite, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Actividad Semanal",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyMinutes.forEachIndexed { index, minutes ->
                    DayBar(
                        day = days[index],
                        minutes = minutes,
                        maxMinutes = maxMinutes
                    )
                }
            }
        }
    }
}

@Composable
private fun DayBar(day: String, minutes: Int, maxMinutes: Int) {
    val height = if (maxMinutes > 0) (minutes.toFloat() / maxMinutes * 100).coerceIn(10f, 100f) else 10f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            text = "${minutes}m",
            fontSize = 8.sp,
            color = TextTertiary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .width(28.dp)
                .fillMaxHeight(height / 100f)
                .background(
                    if (minutes > 0) PrimaryGreen else BorderLight,
                    RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                )
        )

        Text(
            text = day,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}