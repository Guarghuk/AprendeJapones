package com.example.aprendejapones.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

/**
 * Esquema de colores para el tema claro de Kotodama.
 *
 * Define la paleta de colores completa siguiendo Material Design 3.
 * Mapea los colores personalizados de la aplicación a los roles
 * semánticos de Material 3.
 *
 * ## Colores Mapeados
 * - **Primary:** Verde - Acciones principales y elementos destacados
 * - **Secondary:** Naranja - Elementos secundarios y acentos
 * - **Tertiary:** Azul - Información adicional
 * - **Background:** Gris claro - Fondo general
 * - **Surface:** Blanco - Tarjetas y elementos elevados
 * - **Error:** Rojo - Estados de error
 */
private val LightColorScheme = lightColorScheme(
    // Colores primarios
    primary = PrimaryGreen,
    onPrimary = SurfaceWhite,
    primaryContainer = PrimaryGreenLight,
    onPrimaryContainer = PrimaryGreenDark,

    // Colores secundarios
    secondary = AccentOrange,
    onSecondary = SurfaceWhite,
    secondaryContainer = AccentOrangeLight,
    onSecondaryContainer = AccentOrangeDark,

    // Color terciario
    tertiary = AccentBlue,

    // Fondos
    background = BackgroundGray,
    onBackground = TextPrimary,

    // Superficies
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceGray,
    onSurfaceVariant = TextSecondary,

    // Errores
    error = ErrorRed,
    onError = SurfaceWhite,

    // Bordes
    outline = BorderGray,
    outlineVariant = BorderLight
)

/**
 * Tema principal de la aplicación Kotodama.
 *
 * Envuelve el contenido de la aplicación con el tema visual de Kotodama,
 * aplicando la paleta de colores y tipografía definidas.
 *
 * ## Características
 * - Actualmente solo soporta modo claro
 * - Usa Material Design 3 (Material You)
 * - Paleta de colores verde/naranja
 * - Tipografía del sistema
 *
 * ## Uso
 *
 * En el Activity o punto de entrada de Compose:
 *
 * ```kotlin
 * class MainActivity : ComponentActivity() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         setContent {
 *             KotodamaTheme {
 *                 // Contenido de la app
 *                 NavHost(...)
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * Dentro de Composables:
 *
 * ```kotlin
 * @Composable
 * fun MyScreen() {
 *     // Acceder a colores del tema
 *     val primaryColor = MaterialTheme.colorScheme.primary
 *
 *     // Acceder a tipografía
 *     val titleStyle = MaterialTheme.typography.titleLarge
 * }
 * ```
 *
 * @param darkTheme Si se debe usar el tema oscuro (actualmente ignorado).
 * @param content Contenido Composable a envolver con el tema.
 *
 * @see LightColorScheme Paleta de colores del tema claro.
 * @see Typography Configuración de tipografía.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Composable
fun KotodamaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Por ahora solo Light Theme
    // TODO: Implementar tema oscuro cuando sea requerido
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = com.example.aprendejapones.presentation.theme.Typography,
        content = content
    )
}