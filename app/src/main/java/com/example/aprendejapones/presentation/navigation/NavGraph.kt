package com.example.aprendejapones.presentation.navigation

import com.example.aprendejapones.presentation.screens.onboarding.OnboardingScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import com.example.aprendejapones.presentation.screens.profile.UserProfileScreen
import com.example.aprendejapones.presentation.screens.profile.EditProfileScreen
import com.example.aprendejapones.presentation.screens.achievements.AchievementsScreen
import com.example.aprendejapones.presentation.screens.auth.LoginScreen
import com.example.aprendejapones.presentation.screens.auth.RegisterScreen
import com.example.aprendejapones.presentation.screens.newpost.NewPostScreen
import com.example.aprendejapones.presentation.screens.dailygoal.DailyGoalScreen
import com.example.aprendejapones.presentation.screens.reminders.RemindersScreen
import com.example.aprendejapones.presentation.screens.stats.StatsScreen
import com.example.aprendejapones.presentation.screens.sync.SyncScreen
import com.example.aprendejapones.presentation.screens.shop.ShopScreen

/**
 * Composable principal que configura la navegación de la app con animaciones
 * El startDestination es determinado por MainActivity basándose en preferencias
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KotodamaNavGraph(
    startDestination: String = Screen.Home.route,
    navController: NavHostController = rememberNavController()
) {
    // 🔍 Log para debugging
    android.util.Log.d("NavGraph", "🚀 Starting with destination: $startDestination")

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Determinar si mostrar bottom navigation
    val showBottomNav = currentRoute in bottomNavScreens
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
                startDestination = startDestination,
                enterTransition = { defaultEnterTransition() },
                exitTransition = { defaultExitTransition() },
                popEnterTransition = { defaultPopEnterTransition() },
                popExitTransition = { defaultPopExitTransition() }
            ) {
                // ============ AUTENTICACIÓN ============

                composable(Screen.Login.route) {
                    LoginScreen(
                        onLoginSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate(Screen.Register.route)
                        }
                    )
                }

                composable(Screen.Register.route) {
                    RegisterScreen(
                        onRegisterSuccess = {
                            navController.navigate(Screen.Onboarding.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        },
                        onNavigateToLogin = {
                            navController.popBackStack()
                        }
                    )
                }

                // ============ ONBOARDING ============

                composable(
                    route = Screen.Onboarding.route,
                    enterTransition = { fadeIn(animationSpec = tween(500)) },
                    exitTransition = { fadeOut(animationSpec = tween(500)) }
                ) {
                    OnboardingScreen(
                        onComplete = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Onboarding.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                // ============ PANTALLAS PRINCIPALES ============

                composable(
                    route = Screen.Home.route,
                    enterTransition = { fadeIn(animationSpec = tween(200)) },
                    exitTransition = { fadeOut(animationSpec = tween(200)) }
                ) {
                    HomeScreen(
                        onNavigateToLesson = { functionName ->
                            navController.navigate(Screen.Lesson.createRoute(functionName))
                        }
                    )
                }

                composable(
                    route = Screen.Profile.route,
                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                    exitTransition = { fadeOut(animationSpec = tween(300)) }
                ) {
                    ProfileScreen(
                        onNavigateToAchievements = {
                            navController.navigate(Screen.Achievements.route)
                        },
                        onNavigateToStats = {
                            navController.navigate(Screen.Stats.route)
                        }
                    )
                }

                composable(
                    route = Screen.Community.route,
                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                    exitTransition = { fadeOut(animationSpec = tween(300)) }
                ) {
                    CommunityScreen(
                        onNavigateToNewPost = {
                            navController.navigate(Screen.NewPost.route)
                        }
                    )
                }

                composable(
                    route = Screen.Menu.route,
                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                    exitTransition = { fadeOut(animationSpec = tween(300)) }
                ) {
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
                                "sync" -> navController.navigate(Screen.Sync.route)
                                "shop" -> navController.navigate(Screen.Shop.route)
                            }
                        },
                        onNavigateToLogin = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    )
                }

                // ============ PERFILES ============

                composable(
                    route = Screen.UserProfile.route,
                    arguments = listOf(navArgument("userId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId") ?: ""
                    UserProfileScreen(
                        userId = userId,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Sync.route) {
                    SyncScreen(onBack = { navController.popBackStack() })
                }

                composable(Screen.Shop.route) {
                    ShopScreen(onBack = { navController.popBackStack() })
                }

                // ============ PANTALLAS SECUNDARIAS ============

                composable(
                    route = Screen.Lesson.route,
                    arguments = listOf(navArgument("functionName") { type = NavType.StringType }),
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(400)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(400)
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    }
                ) { backStackEntry ->
                    val functionName = backStackEntry.arguments?.getString("functionName") ?: ""
                    LessonScreen(
                        functionName = functionName,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Screen.Achievements.route,
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(400)
                        ) + fadeIn(animationSpec = tween(400))
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(400)
                        ) + fadeOut(animationSpec = tween(400))
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(400)
                        ) + fadeIn(animationSpec = tween(400))
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(400)
                        ) + fadeOut(animationSpec = tween(400))
                    }
                ) {
                    AchievementsScreen(onBack = { navController.popBackStack() })
                }

                composable(
                    route = Screen.NewPost.route,
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )
                    }
                ) {
                    NewPostScreen(onBack = { navController.popBackStack() })
                }

                composable(
                    route = Screen.DailyGoal.route,
                    enterTransition = {
                        scaleIn(initialScale = 0.9f, animationSpec = tween(400)) +
                                fadeIn(animationSpec = tween(400))
                    },
                    exitTransition = {
                        scaleOut(targetScale = 0.9f, animationSpec = tween(400)) +
                                fadeOut(animationSpec = tween(400))
                    },
                    popEnterTransition = {
                        scaleIn(initialScale = 0.9f, animationSpec = tween(400)) +
                                fadeIn(animationSpec = tween(400))
                    },
                    popExitTransition = {
                        scaleOut(targetScale = 0.9f, animationSpec = tween(400)) +
                                fadeOut(animationSpec = tween(400))
                    }
                ) {
                    DailyGoalScreen(onBack = { navController.popBackStack() })
                }

                composable(
                    route = Screen.Reminders.route,
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(400)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    }
                ) {
                    RemindersScreen(onBack = { navController.popBackStack() })
                }

                composable(
                    route = Screen.Stats.route,
                    enterTransition = {
                        fadeIn(animationSpec = tween(500)) +
                                scaleIn(initialScale = 0.95f, animationSpec = tween(500))
                    },
                    exitTransition = {
                        fadeOut(animationSpec = tween(500)) +
                                scaleOut(targetScale = 0.95f, animationSpec = tween(500))
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(500)) +
                                scaleIn(initialScale = 0.95f, animationSpec = tween(500))
                    },
                    popExitTransition = {
                        fadeOut(animationSpec = tween(500)) +
                                scaleOut(targetScale = 0.95f, animationSpec = tween(500))
                    }
                ) {
                    StatsScreen(onBack = { navController.popBackStack() })
                }

                // ============ CONFIGURACIÓN ============

                composable(Screen.EditProfile.route) {
                    EditProfileScreen(
                        onBack = { navController.popBackStack() },
                        onSaveSuccess = { navController.popBackStack() }
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

// ============ FUNCIONES DE ANIMACIÓN POR DEFECTO ============

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentTransitionScope<*>.defaultEnterTransition(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(400)
    ) + fadeIn(animationSpec = tween(400))
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentTransitionScope<*>.defaultExitTransition(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(400)
    ) + fadeOut(animationSpec = tween(400))
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentTransitionScope<*>.defaultPopEnterTransition(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(400)
    ) + fadeIn(animationSpec = tween(400))
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentTransitionScope<*>.defaultPopExitTransition(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(400)
    ) + fadeOut(animationSpec = tween(400))
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