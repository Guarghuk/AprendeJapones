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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aprendejapones.presentation.theme.*

/**
 * Pantalla de Lección
 */
@Composable
fun LessonScreen(
    functionName: String,
    onBack: () -> Unit,
    viewModel: LessonViewModel = hiltViewModel()
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
    val isLastQuestion = state.currentQuestion == state.totalQuestions

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
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                // Card con el contenido de la pregunta
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f)
                        .border(3.dp, BorderGray, RoundedCornerShape(12.dp))
                        .background(SurfaceWhite, RoundedCornerShape(12.dp))
                        .padding(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = question.content,
                        fontSize = 80.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Opciones de respuesta
                Column(
                    modifier = Modifier.padding(top = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    question.options.forEach { option ->
                        OptionButton(
                            text = option,
                            isSelected = state.selectedAnswer == option,
                            isCorrect = state.isAnswered && option == question.correctAnswer,
                            isWrong = state.isAnswered && state.selectedAnswer == option && option != question.correctAnswer,
                            enabled = !state.isAnswered,
                            onClick = {
                                if (!state.isAnswered) {
                                    onEvent(LessonEvent.SelectAnswer(option))
                                }
                            }
                        )
                    }
                }

                // Mensaje de feedback
                if (state.isAnswered) {
                    val isCorrect = state.selectedAnswer == question.correctAnswer
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .border(
                                2.dp,
                                if (isCorrect) PrimaryGreen else ErrorRed,
                                RoundedCornerShape(8.dp)
                            )
                            .background(
                                if (isCorrect) PrimaryGreenLight else ErrorRed.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (isCorrect) "¡Correcto! ✓" else "Incorrecto. La respuesta correcta es: ${question.correctAnswer}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCorrect) PrimaryGreen else ErrorRed,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Navigation
        LessonNavigation(
            canGoPrevious = state.currentQuestion > 1 && !state.isAnswered,
            canVerify = state.selectedAnswer != null && !state.isAnswered,
            canNext = state.isAnswered,
            isLastQuestion = isLastQuestion,
            onPrevious = { onEvent(LessonEvent.PreviousQuestion) },
            onVerify = { onEvent(LessonEvent.VerifyAnswer) },
            onNext = {
                if (isLastQuestion) {
                    onEvent(LessonEvent.FinishLesson)
                } else {
                    onEvent(LessonEvent.NextQuestion)
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
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
    isCorrect: Boolean,
    isWrong: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isCorrect -> PrimaryGreenLight
        isWrong -> ErrorRed.copy(alpha = 0.1f)
        isSelected -> PrimaryGreenLight.copy(alpha = 0.3f)
        else -> SurfaceWhite
    }

    val borderColor = when {
        isCorrect -> PrimaryGreen
        isWrong -> ErrorRed
        isSelected -> PrimaryGreen
        else -> BorderGray
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = TextPrimary,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )

            if (isCorrect) {
                Text("✓", fontSize = 20.sp, color = PrimaryGreen)
            } else if (isWrong) {
                Text("✗", fontSize = 20.sp, color = ErrorRed)
            }
        }
    }
}

@Composable
private fun LessonNavigation(
    canGoPrevious: Boolean,
    canVerify: Boolean,
    canNext: Boolean,
    isLastQuestion: Boolean,
    onPrevious: () -> Unit,
    onVerify: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Botón Anterior
        Button(
            onClick = onPrevious,
            enabled = canGoPrevious,
            modifier = Modifier
                .weight(1f)
                .border(2.dp, BorderGray, RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = SurfaceWhite,
                disabledContainerColor = SurfaceWhite.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "← Anterior",
                color = if (canGoPrevious) TextPrimary else TextTertiary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Botón Verificar o Siguiente
        Button(
            onClick = if (canNext) onNext else onVerify,
            enabled = canVerify || canNext,
            modifier = Modifier
                .weight(1f)
                .border(2.dp, PrimaryGreen, RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen,
                disabledContainerColor = PrimaryGreen.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = when {
                    canNext && isLastQuestion -> "Terminar"
                    canNext -> "Siguiente →"
                    else -> "Verificar"
                },
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
                    text = "Repetir Lección",
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