package com.example.aprendejapones.presentation.navigation

/**
 * Sealed class que define todas las rutas de navegación
 */
sealed class Screen(val route: String) {
    // Pantallas principales (Bottom Navigation)
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Community : Screen("community")
    object Menu : Screen("menu")

    // Pantallas secundarias
    object Lesson : Screen("lesson/{functionName}") {
        fun createRoute(functionName: String) = "lesson/$functionName"
    }

    object Achievements : Screen("achievements")
    object NewPost : Screen("newPost")
    object DailyGoal : Screen("dailyGoal")
    object Reminders : Screen("reminders")
    object Stats : Screen("stats")

    // Configuración
    object EditProfile : Screen("editProfile")
    object Privacy : Screen("privacy")
    object Help : Screen("help")
    object Contact : Screen("contact")
    object Language : Screen("language")

    companion object {
        // Pantallas del bottom navigation
        val bottomNavScreens = listOf(Home, Profile, Community, Menu)
    }
}