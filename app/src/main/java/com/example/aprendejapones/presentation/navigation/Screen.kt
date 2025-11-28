package com.example.aprendejapones.presentation.navigation

/**
 * Sealed class que define todas las rutas de navegación
 */
sealed class Screen(val route: String) {
    // Onboarding
    data object Onboarding : Screen("onboarding")

    // Pantallas principales (Bottom Navigation)
    data object Home : Screen("home")
    data object Profile : Screen("profile")
    data object Community : Screen("community")
    data object Menu : Screen("menu")

    // Pantallas secundarias
    data object Lesson : Screen("lesson/{functionName}") {
        fun createRoute(functionName: String) = "lesson/$functionName"
    }

    data object Achievements : Screen("achievements")
    data object NewPost : Screen("newPost")
    data object DailyGoal : Screen("dailyGoal")
    data object Reminders : Screen("reminders")
    data object Stats : Screen("stats")

    // Configuración
    data object EditProfile : Screen("editProfile")
    data object Privacy : Screen("privacy")
    data object Help : Screen("help")
    data object Contact : Screen("contact")
    data object Language : Screen("language")

    // ✅ Rutas de autenticación
    object Login : Screen("login")
    object Register : Screen("register")

    // ✅ Rutas de perfil
    object UserProfile : Screen("user_profile/{userId}") {
        fun createRoute(userId: String) = "user_profile/$userId"
    }

    // ✅ Sincronización
    object Sync : Screen("sync")

    // ✅ Tienda
    object Shop : Screen("shop")

}

// ✅ Definir fuera de la clase para evitar problemas de inicialización
val bottomNavScreens = listOf(
    Screen.Home.route,
    Screen.Profile.route,
    Screen.Community.route,
    Screen.Menu.route
)