package com.example.aprendejapones.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Configuración de tipografía para la aplicación Kotodama.
 *
 * Define los estilos de texto basados en Material Design 3.
 * Actualmente usa la tipografía por defecto del sistema, pero puede
 * extenderse para usar fuentes personalizadas en el futuro.
 *
 * ## Estilos Disponibles
 * - [bodyLarge]: Texto principal del cuerpo (16sp)
 * - [titleLarge]: Títulos principales y encabezados (22sp)
 * - [labelSmall]: Etiquetas pequeñas y metadatos (11sp)
 *
 * ## Uso
 *
 * ```kotlin
 * Text(
 *     text = "Título",
 *     style = MaterialTheme.typography.titleLarge
 * )
 *
 * Text(
 *     text = "Descripción",
 *     style = MaterialTheme.typography.bodyLarge
 * )
 * ```
 *
 * ## Personalización Futura
 * Para añadir una fuente personalizada:
 * 1. Añadir los archivos de fuente en `res/font/`
 * 2. Crear una FontFamily personalizada
 * 3. Reemplazar FontFamily.Default por la nueva familia
 *
 * @see KotodamaTheme Tema que aplica esta tipografía.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
val Typography = Typography(
    /**
     * Estilo de texto para cuerpo de texto largo.
     *
     * Usado para párrafos, descripciones y contenido principal.
     * - Tamaño: 16sp
     * - Peso: Normal
     * - Altura de línea: 24sp
     */
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    /**
     * Estilo de texto para títulos grandes.
     *
     * Usado para encabezados de sección y títulos de pantalla.
     * - Tamaño: 22sp
     * - Peso: Bold
     * - Altura de línea: 28sp
     */
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    /**
     * Estilo de texto para etiquetas pequeñas.
     *
     * Usado para metadatos, timestamps y texto secundario pequeño.
     * - Tamaño: 11sp
     * - Peso: Medium
     * - Altura de línea: 16sp
     */
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)