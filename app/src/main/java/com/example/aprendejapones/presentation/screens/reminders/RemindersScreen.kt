package com.example.aprendejapones.presentation.screens.reminders

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aprendejapones.presentation.theme.*

@Composable
fun RemindersScreen(
    onBack: () -> Unit,
    viewModel: RemindersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is RemindersEffect.ShowToast -> {
                    // TODO: Mostrar toast
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
        RemindersHeader(
            onBack = onBack,
            isEnabled = state.remindersEnabled
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Master switch
            MasterReminderCard(
                enabled = state.remindersEnabled,
                onToggle = { viewModel.onEvent(RemindersEvent.ToggleMaster(it)) }
            )

            // Contenido animado (solo visible si está activado)
            AnimatedVisibility(
                visible = state.remindersEnabled,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Time selector con números grandes
                    TimePickerCard(
                        selectedHour = state.selectedHour,
                        selectedMinute = state.selectedMinute,
                        onTimeChanged = { hour, minute ->
                            viewModel.onEvent(RemindersEvent.SetTime(hour, minute))
                        }
                    )

                    // Days selector mejorado
                    DaySelectorCard(
                        selectedDays = state.selectedDays,
                        onDayToggled = { day ->
                            viewModel.onEvent(RemindersEvent.ToggleDay(day))
                        }
                    )

                    // Notification style
                    NotificationStyleCard(
                        motivational = state.motivationalMessages,
                        onToggle = { viewModel.onEvent(RemindersEvent.ToggleMotivational(it)) }
                    )

                    // Preview de notificación
                    NotificationPreviewCard(
                        motivational = state.motivationalMessages
                    )
                }
            }

            // Info card
            InfoCard()

            // Save button
            AnimatedVisibility(
                visible = state.remindersEnabled,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Button(
                    onClick = { viewModel.onEvent(RemindersEvent.SaveSettings) },
                    enabled = !state.isSaving && state.selectedDays.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, PrimaryGreen, RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        disabledContainerColor = BorderGray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = SurfaceWhite,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Guardar Configuración",
                            color = SurfaceWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RemindersHeader(
    onBack: () -> Unit,
    isEnabled: Boolean
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
            TextButton(onClick = onBack) {
                Text(
                    text = "← Volver",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🔔",
                    fontSize = 20.sp,
                    modifier = Modifier.scale(if (isEnabled) 1.2f else 1f)
                )
                Text(
                    text = "Recordatorios",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.width(60.dp))
        }
    }
}

@Composable
private fun MasterReminderCard(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (enabled) PrimaryGreenLight else SurfaceWhite,
        animationSpec = tween(300)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (enabled) PrimaryGreen else BorderGray,
                shape = RoundedCornerShape(10.dp)
            )
            .background(backgroundColor, RoundedCornerShape(10.dp))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Activar Recordatorios",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (enabled) "Te notificaremos según tu configuración" else "Recibe recordatorios diarios para estudiar",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Switch mejorado
            Box(
                modifier = Modifier
                    .size(60.dp, 32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (enabled) PrimaryGreen else BorderGray)
                    .clickable { onToggle(!enabled) }
                    .padding(4.dp),
                contentAlignment = if (enabled) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(SurfaceWhite)
                )
            }
        }
    }
}

@Composable
private fun TimePickerCard(
    selectedHour: Int,
    selectedMinute: Int,
    onTimeChanged: (Int, Int) -> Unit
) {
    var showHourPicker by remember { mutableStateOf(false) }
    var showMinutePicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(SurfaceWhite, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Hora del Recordatorio",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Display grande de la hora
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hora
                TimeDigitBox(
                    value = selectedHour,
                    onClick = { showHourPicker = true }
                )

                Text(
                    text = ":",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                // Minuto
                TimeDigitBox(
                    value = selectedMinute,
                    onClick = { showMinutePicker = true }
                )
            }

            // Quick time chips
            Text(
                text = "Accesos rápidos:",
                fontSize = 10.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickTimeChip("07:00", 7, 0, onTimeChanged, Modifier.weight(1f))
                QuickTimeChip("09:00", 9, 0, onTimeChanged, Modifier.weight(1f))
                QuickTimeChip("18:00", 18, 0, onTimeChanged, Modifier.weight(1f))
                QuickTimeChip("20:00", 20, 0, onTimeChanged, Modifier.weight(1f))
            }
        }
    }

    // TODO: Implementar pickers nativos si quieres
}

@Composable
private fun TimeDigitBox(
    value: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp, 80.dp)
            .border(3.dp, PrimaryGreen, RoundedCornerShape(12.dp))
            .background(PrimaryGreenLight, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = String.format("%02d", value),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )
    }
}

@Composable
private fun QuickTimeChip(
    label: String,
    hour: Int,
    minute: Int,
    onTimeChanged: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onTimeChanged(hour, minute) },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = SurfaceGray
        ),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextPrimary
        )
    }
}

@Composable
private fun DaySelectorCard(
    selectedDays: Set<String>,
    onDayToggled: (String) -> Unit
) {
    val days = listOf(
        "L" to "Lunes",
        "M" to "Martes",
        "X" to "Miércoles",
        "J" to "Jueves",
        "V" to "Viernes",
        "S" to "Sábado",
        "D" to "Domingo"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(SurfaceWhite, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Días de la Semana",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${selectedDays.size} seleccionados",
                    fontSize = 10.sp,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                days.forEach { (letter, fullName) ->
                    DayChip(
                        letter = letter,
                        fullName = fullName,
                        isSelected = selectedDays.contains(fullName),
                        onClick = { onDayToggled(fullName) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DayChip(
    letter: String,
    fullName: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(if (isSelected) 1.05f else 1f)

    Button(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .border(
                width = if (isSelected) 3.dp else 2.dp,
                color = if (isSelected) PrimaryGreen else BorderGray,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) PrimaryGreen else SurfaceWhite
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = letter,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) SurfaceWhite else TextSecondary
            )
        }
    }
}

@Composable
private fun NotificationStyleCard(
    motivational: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(SurfaceWhite, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("✨", fontSize = 20.sp)
                    Text(
                        text = "Mensajes Motivacionales",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Text(
                    text = if (motivational) "Recibirás mensajes de ánimo 💪" else "Solo recordatorios simples",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 28.dp)
                )
            }

            Switch(
                checked = motivational,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = PrimaryGreen,
                    checkedTrackColor = PrimaryGreenLight
                )
            )
        }
    }
}

@Composable
private fun NotificationPreviewCard(motivational: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, AccentBlue.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .background(AccentBlue.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📱", fontSize = 20.sp)
                Text(
                    text = "Vista Previa",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .border(1.dp, BorderGray, RoundedCornerShape(8.dp))
                    .background(SurfaceWhite, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row {
                    Text("🌸", fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
                    Column {
                        Text(
                            text = "Kotodama",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (motivational) {
                                "¡Cada día estás más cerca de tu meta! 頑張って！"
                            } else {
                                "No olvides tu lección de japonés de hoy"
                            },
                            fontSize = 10.sp,
                            color = TextSecondary,
                            lineHeight = 14.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
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
            .border(2.dp, AccentOrange.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .background(AccentOrangeLight, RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Text("💡", fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
            Column {
                Text(
                    text = "Sobre los recordatorios",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentOrangeDark
                )
                Text(
                    text = "Los recordatorios te ayudarán a mantener tu racha diaria. Recibirás una notificación en los días y hora seleccionados. Si no estudias, te enviaremos una alerta sobre tu racha en peligro 🔥",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}