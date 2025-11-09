package com.example.aprendejapones.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aprendejapones.presentation.screens.community.CommunityScreen
import com.example.aprendejapones.presentation.screens.home.HomeScreen
import com.example.aprendejapones.presentation.screens.lesson.LessonScreen
import com.example.aprendejapones.presentation.screens.menu.MenuScreen
import com.example.aprendejapones.presentation.screens.profile.ProfileScreen

/**
 * Composable principal que configura la navegación de la app
 */
@Composable
fun KotodamaNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Determinar si mostrar bottom navigation
    val showBottomNav = currentRoute in Screen.bottomNavScreens.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                // ============ PANTALLAS PRINCIPALES ============

                // Home Screen
                composable(Screen.Home.route) {
                    HomeScreen(
                        onNavigateToLesson = { functionName ->
                            navController.navigate(Screen.Lesson.createRoute(functionName))
                        }
                    )
                }

                // Profile Screen
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        onNavigateToAchievements = {
                            navController.navigate(Screen.Achievements.route)
                        },
                        onNavigateToStats = {
                            navController.navigate(Screen.Stats.route)
                        }
                    )
                }

                // Community Screen
                composable(Screen.Community.route) {
                    CommunityScreen(
                        onNavigateToNewPost = {
                            navController.navigate(Screen.NewPost.route)
                        }
                    )
                }

                // Menu Screen
                composable(Screen.Menu.route) {
                    MenuScreen(
                        onNavigateToScreen = { screenRoute ->
                            when (screenRoute) {
                                "goal" -> navController.navigate(Screen.DailyGoal.route)
                                "reminders" -> navController.navigate(Screen.Reminders.route)
                                "stats" -> navController.navigate(Screen.Stats.route)
                                "editProfile" -> navController.navigate(Screen.EditProfile.route)
                                "privacy" -> navController.navigate(Screen.Privacy.route)
                                "help" -> navController.navigate(Screen.Help.route)
                                "contact" -> navController.navigate(Screen.Contact.route)
                                "language" -> navController.navigate(Screen.Language.route)
                            }
                        }
                    )
                }

                // ============ PANTALLAS SECUNDARIAS ============

                // Lesson Screen (con parámetro)
                composable(
                    route = Screen.Lesson.route,
                    arguments = listOf(
                        navArgument("functionName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val functionName = backStackEntry.arguments?.getString("functionName") ?: ""
                    LessonScreen(
                        functionName = functionName,
                        onBack = { navController.popBackStack() }
                    )
                }

                // Achievements Screen
                composable(Screen.Achievements.route) {
                    PlaceholderScreen(
                        title = "Logros",
                        onBack = { navController.popBackStack() }
                    )
                }

                // New Post Screen
                composable(Screen.NewPost.route) {
                    PlaceholderScreen(
                        title = "Nueva Publicación",
                        onBack = { navController.popBackStack() }
                    )
                }

                // Daily Goal Screen
                composable(Screen.DailyGoal.route) {
                    PlaceholderScreen(
                        title = "Meta Diaria",
                        onBack = { navController.popBackStack() }
                    )
                }

                // Reminders Screen
                composable(Screen.Reminders.route) {
                    PlaceholderScreen(
                        title = "Recordatorios",
                        onBack = { navController.popBackStack() }
                    )
                }

                // Stats Screen
                composable(Screen.Stats.route) {
                    PlaceholderScreen(
                        title = "Estadísticas",
                        onBack = { navController.popBackStack() }
                    )
                }

                // ============ CONFIGURACIÓN ============

                composable(Screen.EditProfile.route) {
                    PlaceholderScreen(
                        title = "Editar Perfil",
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Privacy.route) {
                    PlaceholderScreen(
                        title = "Privacidad",
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Help.route) {
                    PlaceholderScreen(
                        title = "Ayuda",
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Contact.route) {
                    PlaceholderScreen(
                        title = "Contacto",
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Language.route) {
                    PlaceholderScreen(
                        title = "Idioma",
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

/**
 * Pantalla placeholder para rutas pendientes de implementar
 */
@Composable
private fun PlaceholderScreen(
    title: String,
    onBack: () -> Unit
) {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        androidx.compose.material3.Text(
            text = "🚧",
            fontSize = 64.sp
        )
        androidx.compose.material3.Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        androidx.compose.material3.Text(
            text = "En construcción",
            fontSize = 14.sp,
            color = com.example.aprendejapones.presentation.theme.TextSecondary,
            modifier = Modifier.padding(top = 8.dp)
        )
        androidx.compose.material3.Button(
            onClick = onBack,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            androidx.compose.material3.Text("← Volver")
        }
    }
}