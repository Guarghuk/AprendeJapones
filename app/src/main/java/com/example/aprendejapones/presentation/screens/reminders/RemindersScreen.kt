package com.example.aprendejapones.presentation.screens.reminders

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aprendejapones.presentation.theme.*

@Composable
fun RemindersScreen(
    onBack: () -> Unit,
    viewModel: RemindersViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        // Header
        RemindersHeader(onBack = onBack)

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Master switch
            MasterReminderSwitch(
                enabled = state.remindersEnabled,
                onToggle = { viewModel.onEvent(RemindersEvent.ToggleMaster(it)) }
            )

            if (state.remindersEnabled) {
                // Time selector
                TimeSelector(
                    selectedHour = state.selectedHour,
                    selectedMinute = state.selectedMinute,
                    onTimeChanged = { hour, minute ->
                        viewModel.onEvent(RemindersEvent.SetTime(hour, minute))
                    }
                )

                // Days of week
                DaysSelector(
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
            }

            // Info card
            InfoCard()

            // Save button
            Button(
                onClick = { viewModel.onEvent(RemindersEvent.SaveSettings) },
                enabled = state.remindersEnabled,
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

@Composable
private fun RemindersHeader(onBack: () -> Unit) {
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
                text = "🔔 Recordatorios",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.width(60.dp))
        }
    }
}

@Composable
private fun MasterReminderSwitch(
    enabled: Boolean,
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
                Text(
                    text = "Activar Recordatorios",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Recibe notificaciones para no olvidar practicar",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Switch(
                checked = enabled,
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
private fun TimeSelector(
    selectedHour: Int,
    selectedMinute: Int,
    onTimeChanged: (Int, Int) -> Unit
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
                text = "Hora del Recordatorio",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hour selector
                Box(
                    modifier = Modifier
                        .border(2.dp, PrimaryGreen, RoundedCornerShape(8.dp))
                        .background(PrimaryGreenLight, RoundedCornerShape(8.dp))
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = String.format("%02d", selectedHour),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                }

                Text(
                    text = ":",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                // Minute selector
                Box(
                    modifier = Modifier
                        .border(2.dp, PrimaryGreen, RoundedCornerShape(8.dp))
                        .background(PrimaryGreenLight, RoundedCornerShape(8.dp))
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = String.format("%02d", selectedMinute),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                }
            }

            // Time picker buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestedTimeChip("09:00", 9, 0, onTimeChanged, Modifier.weight(1f))
                SuggestedTimeChip("12:00", 12, 0, onTimeChanged, Modifier.weight(1f))
                SuggestedTimeChip("18:00", 18, 0, onTimeChanged, Modifier.weight(1f))
                SuggestedTimeChip("20:00", 20, 0, onTimeChanged, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SuggestedTimeChip(
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
private fun DaysSelector(
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
            Text(
                text = "Días de la Semana",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                days.forEach { (letter, fullName) ->
                    DayChip(
                        letter = letter,
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
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) PrimaryGreen else BorderGray
    val backgroundColor = if (isSelected) PrimaryGreen else SurfaceWhite
    val textColor = if (isSelected) SurfaceWhite else TextSecondary

    Button(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1f)
            .border(2.dp, borderColor, RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = letter,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
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
                Text(
                    text = "Mensajes Motivacionales",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Incluir frases motivadoras en las notificaciones",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    lineHeight = 13.sp,
                    modifier = Modifier.padding(top = 2.dp)
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
private fun InfoCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, AccentBlue.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .background(AccentBlue.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("📱", fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
            Text(
                text = "Los recordatorios te ayudarán a mantener tu racha diaria. Recibirás una notificación en tu dispositivo a la hora seleccionada.",
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }
    }
}