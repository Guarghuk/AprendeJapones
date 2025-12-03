package com.example.aprendejapones.presentation.theme

import androidx.compose.ui.graphics.Color

// ============================================================================
// PALETA DE COLORES DE KOTODAMA
// ============================================================================
//
// Esta sección define todos los colores utilizados en la aplicación Kotodama.
// Los colores están organizados por categoría para facilitar su uso y
// mantenimiento.
//
// ## Uso
// ```kotlin
// Box(modifier = Modifier.background(PrimaryGreen))
// Text(text = "Hola", color = TextPrimary)
// ```
// ============================================================================

// ============ Colores Primarios (Verde) ============

/**
 * Color primario de la aplicación - Verde principal.
 * Usado para botones principales, barras de navegación y elementos destacados.
 */
val PrimaryGreen = Color(0xFF4CAF50)

/**
 * Variante oscura del verde primario.
 * Usado para estados pressed y elementos que necesitan más contraste.
 */
val PrimaryGreenDark = Color(0xFF388E3C)

/**
 * Variante clara del verde primario.
 * Usado para fondos de contenedores y estados deshabilitados.
 */
val PrimaryGreenLight = Color(0xFFE8F5E9)

// ============ Colores de Acento ============

/**
 * Color de acento - Naranja.
 * Usado para elementos secundarios destacados y botones alternativos.
 */
val AccentOrange = Color(0xFFFF9800)

/**
 * Variante clara del naranja de acento.
 * Usado para fondos de alertas y notificaciones suaves.
 */
val AccentOrangeLight = Color(0xFFFFF8E1)

/**
 * Variante oscura del naranja de acento.
 * Usado para iconos y textos sobre fondo naranja claro.
 */
val AccentOrangeDark = Color(0xFFFF6F00)

/**
 * Color de acento - Rojo.
 * Usado para acciones destructivas y estados de error.
 */
val AccentRed = Color(0xFFFF5722)

/**
 * Color de acento - Azul.
 * Usado para enlaces e información secundaria.
 */
val AccentBlue = Color(0xFF2196F3)

/**
 * Color de acento - Rosa.
 * Usado para elementos decorativos y badges.
 */
val AccentPink = Color(0xFFE91E63)

// ============ Fondos y Superficies ============

/**
 * Color de fondo principal de la aplicación.
 * Gris muy claro para separar el contenido de los bordes de la pantalla.
 */
val BackgroundGray = Color(0xFFF5F5F5)

/**
 * Color de superficie - Blanco puro.
 * Usado para tarjetas, diálogos y elementos elevados.
 */
val SurfaceWhite = Color.White

/**
 * Color de superficie secundario - Gris muy claro.
 * Usado para fondos de elementos interactivos y secciones alternadas.
 */
val SurfaceGray = Color(0xFFFAFAFA)

// ============ Colores de Texto ============

/**
 * Color de texto primario - Negro.
 * Usado para títulos y texto principal.
 */
val TextPrimary = Color.Black

/**
 * Color de texto secundario - Gris oscuro.
 * Usado para subtítulos y descripciones.
 */
val TextSecondary = Color(0xFF666666)

/**
 * Color de texto terciario - Gris claro.
 * Usado para placeholders y texto de baja importancia.
 */
val TextTertiary = Color(0xFF999999)

// ============ Bordes y Divisores ============

/**
 * Color de borde principal - Gris medio.
 * Usado para bordes de campos de texto y divisores visibles.
 */
val BorderGray = Color(0xFFDDDDDD)

/**
 * Color de borde secundario - Gris muy claro.
 * Usado para separadores sutiles y bordes de secciones.
 */
val BorderLight = Color(0xFFEEEEEE)

// ============ Colores de Estado ============

/**
 * Color de éxito - Verde.
 * Usado para indicar operaciones exitosas y estados positivos.
 */
val SuccessGreen = Color(0xFF4CAF50)

/**
 * Color de error - Rojo claro.
 * Usado para mensajes de error y validaciones fallidas.
 */
val ErrorRed = Color(0xFFE57373)

/**
 * Color de advertencia - Amarillo/Naranja.
 * Usado para alertas y estados que requieren atención.
 */
val WarningYellow = Color(0xFFFFB74D)

/**
 * Color informativo - Azul claro.
 * Usado para mensajes informativos y tips.
 */
val InfoBlue = Color(0xFF64B5F6)