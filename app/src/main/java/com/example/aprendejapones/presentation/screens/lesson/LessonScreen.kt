package com.example.aprendejapones.presentation.screens.lesson

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aprendejapones.presentation.theme.*

/**
 * Pantalla de Lección
 */
@Composable
fun LessonScreen(
    functionName: String,
    onBack: () -> Unit,
    viewModel: LessonViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(functionName) {
        viewModel.onEvent(LessonEvent.LoadLesson(functionName))
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is LessonEffect.NavigateBack -> onBack()
                is LessonEffect.ShowToast -> {
                    // TODO: Toast
                }
            }
        }
    }

    if (state.showResults) {
        ResultsScreen(
            correctAnswers = state.correctAnswers,
            totalQuestions = state.totalQuestions,
            onBack = onBack,
            onContinue = { viewModel.onEvent(LessonEvent.RestartLesson) }
        )
    } else {
        LessonContent(
            state = state,
            onEvent = viewModel::onEvent,
            onBack = onBack
        )
    }
}

@Composable
private fun LessonContent(
    state: LessonState,
    onEvent: (LessonEvent) -> Unit,
    onBack: () -> Unit
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

    val currentQuestion = state.questions.getOrNull(state.currentQuestion - 1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        // Header
        LessonHeader(
            functionName = state.functionName,
            currentQuestion = state.currentQuestion,
            totalQuestions = state.totalQuestions,
            progress = state.currentQuestion / state.totalQuestions.toFloat(),
            onBack = onBack
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            currentQuestion?.let { question ->
                Text(
                    text = question.text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp),
                    color = TextPrimary
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f)
                        .border(3.dp, BorderGray, RoundedCornerShape(12.dp))
                        .background(SurfaceWhite, RoundedCornerShape(12.dp))
                        .padding(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = question.content, fontSize = 80.sp)
                }

                Column(
                    modifier = Modifier.padding(top = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    question.options.forEach { option ->
                        OptionButton(
                            text = option,
                            isSelected = state.selectedAnswer == option,
                            onClick = { onEvent(LessonEvent.SelectAnswer(option)) }
                        )
                    }
                }
            }
        }

        // Navigation
        LessonNavigation(
            canGoPrevious = state.currentQuestion > 1,
            canVerify = state.selectedAnswer != null && !state.isAnswered,
            isLastQuestion = state.currentQuestion == state.totalQuestions,
            onPrevious = { onEvent(LessonEvent.PreviousQuestion) },
            onVerify = {
                if (state.isAnswered) {
                    onEvent(LessonEvent.NextQuestion)
                } else {
                    onEvent(LessonEvent.VerifyAnswer)
                }
            }
        )
    }
}

@Composable
private fun LessonHeader(
    functionName: String,
    currentQuestion: Int,
    totalQuestions: Int,
    progress: Float,
    onBack: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceWhite)
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) {
                    Text(
                        text = "✕ Salir",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = functionName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Pregunta $currentQuestion/$totalQuestions",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.width(50.dp))
            }
        }

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(8.dp)
                .border(2.dp, BorderGray, RoundedCornerShape(4.dp))
                .background(BackgroundGray, RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .background(PrimaryGreen, RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
private fun OptionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                2.dp,
                if (isSelected) PrimaryGreen else BorderGray,
                RoundedCornerShape(8.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) PrimaryGreenLight else SurfaceWhite
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(
            text = text,
            color = TextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun LessonNavigation(
    canGoPrevious: Boolean,
    canVerify: Boolean,
    isLastQuestion: Boolean,
    onPrevious: () -> Unit,
    onVerify: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onPrevious,
            enabled = canGoPrevious,
            modifier = Modifier
                .weight(1f)
                .border(2.dp, BorderGray, RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = SurfaceWhite),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "← Anterior",
                color = if (canGoPrevious) TextPrimary else TextTertiary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onVerify,
            enabled = canVerify,
            modifier = Modifier
                .weight(1f)
                .border(2.dp, PrimaryGreen, RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = if (isLastQuestion) "Terminar" else "Verificar →",
                color = SurfaceWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ResultsScreen(
    correctAnswers: Int,
    totalQuestions: Int,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    val accuracy = (correctAnswers.toFloat() / totalQuestions * 100).toInt()
    val xpEarned = correctAnswers * 5

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(PrimaryGreenLight, RoundedCornerShape(50.dp))
                .border(3.dp, PrimaryGreen, RoundedCornerShape(50.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("🎉", fontSize = 60.sp)
        }

        Text(
            text = "¡Excelente trabajo!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp),
            color = TextPrimary
        )

        Text(
            text = "Has completado la lección",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center
        )

        // Stats
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
                .background(SurfaceWhite, RoundedCornerShape(10.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ResultStat("$correctAnswers/$totalQuestions", "Correctas", Modifier.weight(1f))
                ResultStat("$accuracy%", "Precisión", Modifier.weight(1f), PrimaryGreen)
                ResultStat("+$xpEarned", "XP Ganados", Modifier.weight(1f), AccentBlue)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, PrimaryGreen, RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "Continuar Aprendiendo",
                    color = SurfaceWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, BorderGray, RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceWhite),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "Volver al Inicio",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun ResultStat(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    color: Color = TextPrimary
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}