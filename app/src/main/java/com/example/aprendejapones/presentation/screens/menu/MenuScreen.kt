package com.example.aprendejapones.presentation.screens.menu

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

/**
 * Pantalla de Menú
 */
@Composable
fun MenuScreen(
    onNavigateToScreen: (String) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    viewModel: MenuViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Manejar efectos
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is MenuEffect.NavigateToScreen -> onNavigateToScreen(effect.screen)
                is MenuEffect.NavigateToLogin -> onNavigateToLogin()
                is MenuEffect.ShowToast -> {
                    // Toast handled elsewhere
                }
            }
        }
    }

    MenuContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun MenuContent(
    state: MenuState,
    onEvent: (MenuEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
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
                    text = "☰ Menú",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Configuración y opciones",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Sección Cuenta
            MenuSection(
                title = "Cuenta",
                items = listOf(
                    MenuItem("👤", "Editar Perfil", "Nombre, avatar y más", "editProfile"),
                    MenuItem("🔐", "Privacidad y Seguridad", "Contraseña y datos", "privacy"),
                    MenuItem("☁️", "Sincronizar Datos", "Sincroniza con la nube", "sync")
                ),
                onItemClick = { screen -> onEvent(MenuEvent.NavigateToScreen(screen)) }
            )

            // Sección Aprendizaje
            MenuSection(
                title = "Aprendizaje",
                items = listOf(
                    MenuItem("🎯", "Meta Diaria", "Actualmente: ${state.dailyGoalMinutes} min/día", "goal"),
                    MenuItem("🔔", "Recordatorios", "Configura notificaciones", "reminders"),
                    MenuItem("📊", "Estadísticas Completas", "Ver todo tu progreso", "stats"),
                    MenuItem("🛍️", "Tienda", "Compra escudos, insignias y más", "shop")
                ),
                onItemClick = { screen -> onEvent(MenuEvent.NavigateToScreen(screen)) }
            )

            // Sección Aplicación
            MenuSection(
                title = "Aplicación",
                items = listOf(
                    MenuItem("🌙", "Modo Oscuro", if (state.isDarkMode) "Activado" else "Apagado", "darkMode"),
                    MenuItem("🔊", "Sonidos", if (state.isSoundEnabled) "Activados" else "Desactivados", "sound"),
                    MenuItem("🌐", "Idioma de la App", state.appLanguage, "language")
                ),
                onItemClick = { screen -> onEvent(MenuEvent.NavigateToScreen(screen)) }
            )

            // Sección Soporte
            MenuSection(
                title = "Soporte",
                items = listOf(
                    MenuItem("❓", "Centro de Ayuda", "FAQs y tutoriales", "help"),
                    MenuItem("💌", "Contactar Soporte", "Envía tus dudas", "contact"),
                    MenuItem("⭐", "Califica la App", "Déjanos tu opinión", "rate")
                ),
                onItemClick = { screen -> onEvent(MenuEvent.NavigateToScreen(screen)) }
            )

            // Botón Cerrar Sesión
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, ErrorRed, RoundedCornerShape(8.dp))
                    .background(ErrorRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .clickable { onEvent(MenuEvent.Logout) }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🚪 Cerrar Sesión",
                    color = ErrorRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Footer
            Text(
                text = "Kotodama v1.0.0\n© 2025 - Todos los derechos reservados",
                fontSize = 10.sp,
                color = TextTertiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MenuSection(
    title: String,
    items: List<MenuItem>,
    onItemClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, BorderGray, RoundedCornerShape(10.dp))
            .background(SurfaceWhite, RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )

            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onItemClick(item.route) },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = item.icon,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column {
                            Text(
                                text = item.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = item.subtitle,
                                fontSize = 10.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "→",
                        fontSize = 14.sp,
                        color = PrimaryGreen
                    )
                }

                if (index < items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = BorderLight
                    )
                }
            }
        }
    }
}

data class MenuItem(
    val icon: String,
    val title: String,
    val subtitle: String,
    val route: String
)