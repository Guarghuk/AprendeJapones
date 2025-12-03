package com.example.aprendejapones.presentation.navigation

/**
 * Clase sellada que define todas las rutas de navegación de la aplicación.
 *
 * Utiliza el patrón sealed class para representar de forma type-safe
 * todas las pantallas disponibles en la aplicación. Cada pantalla tiene
 * una ruta única que se usa con Jetpack Navigation Compose.
 *
 * ## Categorías de Pantallas
 * - **Onboarding:** Introducción para nuevos usuarios
 * - **Bottom Navigation:** Pantallas principales con navegación inferior
 * - **Secundarias:** Pantallas accesibles desde las principales
 * - **Autenticación:** Login y registro
 * - **Perfil:** Configuración y perfil de usuario
 *
 * ## Rutas con Parámetros
 * Algunas pantallas requieren parámetros en la ruta:
 * - [Lesson]: Requiere `functionName` para saber qué lección mostrar
 * - [UserProfile]: Requiere `userId` para mostrar el perfil de un usuario
 *
 * ## Uso
 *
 * ```kotlin
 * // Navegación simple
 * navController.navigate(Screen.Home.route)
 *
 * // Navegación con parámetros
 * navController.navigate(Screen.Lesson.createRoute("Hiragana"))
 * navController.navigate(Screen.UserProfile.createRoute(userId))
 * ```
 *
 * @property route String de la ruta para Navigation Compose.
 *
 * @see NavGraph Para la configuración del grafo de navegación.
 * @see BottomNavigationBar Para la navegación inferior.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
sealed class Screen(val route: String) {

    // ============ Onboarding ============

    /** Pantalla de introducción para nuevos usuarios */
    data object Onboarding : Screen("onboarding")

    // ============ Pantallas Principales (Bottom Navigation) ============

    /** Pantalla principal con estadísticas y acceso a lecciones */
    data object Home : Screen("home")

    /** Pantalla de perfil del usuario actual */
    data object Profile : Screen("profile")

    /** Pantalla de comunidad con publicaciones y comentarios */
    data object Community : Screen("community")

    /** Pantalla de menú con configuraciones y opciones */
    data object Menu : Screen("menu")

    // ============ Pantallas Secundarias ============

    /**
     * Pantalla de lección con preguntas y ejercicios.
     *
     * Requiere el nombre de la función/lección como parámetro.
     *
     * ## Uso
     * ```kotlin
     * navController.navigate(Screen.Lesson.createRoute("Hiragana"))
     * ```
     */
    data object Lesson : Screen("lesson/{functionName}") {
        /**
         * Crea la ruta con el nombre de la función.
         * @param functionName Nombre de la lección (ej: "Hiragana", "Kanji").
         * @return Ruta completa para navegación.
         */
        fun createRoute(functionName: String) = "lesson/$functionName"
    }

    /** Pantalla de logros del usuario */
    data object Achievements : Screen("achievements")

    /** Pantalla para crear una nueva publicación */
    data object NewPost : Screen("newPost")

    /** Pantalla de configuración de meta diaria */
    data object DailyGoal : Screen("dailyGoal")

    /** Pantalla de configuración de recordatorios */
    data object Reminders : Screen("reminders")

    /** Pantalla de estadísticas detalladas */
    data object Stats : Screen("stats")

    // ============ Configuración ============

    /** Pantalla de edición del perfil */
    data object EditProfile : Screen("editProfile")

    /** Pantalla de configuración de privacidad */
    data object Privacy : Screen("privacy")

    /** Pantalla de ayuda y FAQ */
    data object Help : Screen("help")

    /** Pantalla de contacto y soporte */
    data object Contact : Screen("contact")

    /** Pantalla de configuración de idioma */
    data object Language : Screen("language")

    // ============ Rutas de Autenticación ============

    /** Pantalla de inicio de sesión */
    object Login : Screen("login")

    /** Pantalla de registro de nuevo usuario */
    object Register : Screen("register")

    // ============ Rutas de Perfil ============

    /**
     * Pantalla de perfil de otro usuario.
     *
     * Requiere el ID del usuario como parámetro.
     *
     * ## Uso
     * ```kotlin
     * navController.navigate(Screen.UserProfile.createRoute(authorId))
     * ```
     */
    object UserProfile : Screen("user_profile/{userId}") {
        /**
         * Crea la ruta con el ID del usuario.
         * @param userId ID del usuario cuyo perfil se quiere ver.
         * @return Ruta completa para navegación.
         */
        fun createRoute(userId: String) = "user_profile/$userId"
    }

    // ============ Sincronización ============

    /** Pantalla de sincronización de datos con la nube */
    object Sync : Screen("sync")

    // ============ Tienda ============

    /** Pantalla de tienda para comprar items con monedas */
    object Shop : Screen("shop")

}

/**
 * Lista de rutas que aparecen en la navegación inferior (Bottom Navigation).
 *
 * Estas pantallas tienen iconos en la barra de navegación inferior
 * y son accesibles desde cualquier punto de la aplicación.
 *
 * @see BottomNavigationBar Componente que usa esta lista.
 */
val bottomNavScreens = listOf(
    Screen.Home.route,
    Screen.Profile.route,
    Screen.Community.route,
    Screen.Menu.route
)