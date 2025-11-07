package com.example.aprendejapones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun KotodamaApp() {
    var currentScreen by remember { mutableStateOf("home") }
    var selectedFunction by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        when {
            selectedFunction != null -> LessonScreen(
                functionName = selectedFunction!!,
                onBack = { selectedFunction = null }
            )
            else -> MainScreen(
                currentScreen = currentScreen,
                onNavigate = { currentScreen = it },
                onSelectFunction = { selectedFunction = it }
            )
        }
    }
}

@Composable
fun MainScreen(
    currentScreen: String,
    onNavigate: (String) -> Unit,
    onSelectFunction: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (currentScreen) {
            "home" -> HomeScreen(onSelectFunction)
            "profile" -> ProfileScreen(onNavigate)
            "community" -> CommunityScreen(onNavigate)
            "menu" -> MenuScreen(onNavigate)
            "achievements" -> AchievementsScreen(onBack = { onNavigate("profile") })
            "newPost" -> NewPostScreen(onBack = { onNavigate("community") })
            "goal" -> DailyGoalScreen(onBack = { onNavigate("menu") })
            "reminders" -> RemindersScreen(onBack = { onNavigate("menu") })
            //"stats" -> CompleteStatsScreen(onBack = { onNavigate("menu") })
        }

        BottomNavigation(
            currentScreen = currentScreen,
            onNavigate = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// =====================================================
// PANTALLA DE INICIO (Mejorada)
// =====================================================
@Composable
fun HomeScreen(onSelectFunction: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        HeaderSection()
        TreeSection()
        KitsuneMessage()
        FunctionsGrid(onSelectFunction)
        DailyChallengeBox()
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(width = 0.dp, color = Color.Transparent)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF4CAF50), RoundedCornerShape(24.dp))
                        .border(2.dp, Color(0xFF388E3C), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("K", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text("Rango", fontSize = 9.sp, color = Color(0xFF999999))
                    Text("初心者", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatBadge("🔥 7", Color(0xFFFF5722))
                StatBadge("💧 150", Color(0xFF2196F3))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(18.dp))
                        .background(Color(0xFFFAFAFA), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔔", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun StatBadge(text: String, accentColor: Color) {
    Box(
        modifier = Modifier
            .border(2.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
            .background(accentColor.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentColor.copy(alpha = 0.8f))
    }
}

@Composable
fun TreeSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(28.dp),
        Alignment.Center

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🌳", fontSize = 64.sp, modifier = Modifier.padding(bottom = 12.dp))
            Text(
                "Tu Santuario Digital",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                "こんにちは！",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun KitsuneMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(2.dp, Color(0xFFFFB74D).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .background(Color(0xFFFFF8E1), RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(2.dp, Color(0xFFFF9800), RoundedCornerShape(20.dp))
                    .background(Color.White, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🦊", fontSize = 22.sp)
            }

            Column {
                Text(
                    "Kitsune-sensei",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6F00)
                )
                Text(
                    "¡Buenos días! Hoy es perfecto para practicar.",
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun FunctionsGrid(onSelectFunction: (String) -> Unit) {
    val functions = listOf(
        Triple("💬", "Haz Frases", "Nuevas palabras"),
        Triple("📚", "Vocabulario", "Palabras esenciales"),
        Triple("あ", "Hiragana", "Sistema silábico"),
        Triple("ア", "Katakana", "Palabras extranjeras"),
        Triple("漢", "Kanji", "Caracteres japoneses"),
        Triple("🗣️", "Conversación", "Habla con IA"),
        Triple("🎤", "Pronunciación", "Escucha y repite"),
        Triple("📖", "Gramática", "Estructuras y partículas")
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Funciones",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 14.dp),
            color = Color.Black
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            for (i in functions.indices step 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        FunctionCard(functions[i].first, functions[i].second, functions[i].third) {
                            onSelectFunction(functions[i].second)
                        }
                    }

                    if (i + 1 < functions.size) {
                        Box(modifier = Modifier.weight(1f)) {
                            FunctionCard(functions[i + 1].first, functions[i + 1].second, functions[i + 1].third) {
                                onSelectFunction(functions[i + 1].second)
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun FunctionCard(icon: String, name: String, subtitle: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(115.dp)
            .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(10.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                subtitle,
                fontSize = 9.sp,
                color = Color(0xFF999999),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun DailyChallengeBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Desafío Diario",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        "Completa 5 actividades",
                        fontSize = 11.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(6.dp))
                        .background(Color(0xFFE8F5E9), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("23:45:12", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(12.dp)
                    .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(6.dp))
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(6.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight()
                        .background(Color(0xFF4CAF50), RoundedCornerShape(6.dp))
                )
            }

            Text(
                "3/5 completadas • +50 精",
                fontSize = 10.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// =====================================================
// PANTALLA DE PERFIL (Mejorada)
// =====================================================
@Composable
fun ProfileScreen(onNavigate: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("👤 Tu Perfil", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text("Logros y Progreso", fontSize = 11.sp, color = Color(0xFF666666), modifier = Modifier.padding(top = 4.dp))
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            UserInfoCard()
            StatsCard(onNavigate)
            AchievementsPreviewCard(onNavigate)
            RecentActivityCard()
        }
    }
}

@Composable
fun UserInfoCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .border(3.dp, Color(0xFF4CAF50), RoundedCornerShape(30.dp))
                        .background(Color(0xFF4CAF50), RoundedCornerShape(30.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("K", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Column(modifier = Modifier.padding(start = 14.dp)) {
                    Text(
                        "Usuario123",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        "Rango: 初心者",
                        fontSize = 11.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        "Miembro desde: Enero 2025",
                        fontSize = 10.sp,
                        color = Color(0xFF999999),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                "Experiencia Total",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Nivel 8", fontSize = 11.sp, color = Color.Black)
                Text("1,450 / 2,000 XP", fontSize = 11.sp, color = Color(0xFF666666))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(10.dp)
                    .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(5.dp))
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(5.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .fillMaxHeight()
                        .background(Color(0xFF4CAF50), RoundedCornerShape(5.dp))
                )
            }
        }
    }
}

@Composable
fun StatsCard(onNavigate: (String) -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .clickable { onNavigate("stats") }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Estadísticas Generales",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text("→", fontSize = 16.sp, color = Color(0xFF4CAF50))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🔥", fontSize = 28.sp)
                    Text(
                        "15",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        "Racha",
                        fontSize = 10.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📚", fontSize = 28.sp)
                    Text(
                        "156",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        "Lecciones",
                        fontSize = 10.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("⏱️", fontSize = 28.sp)
                    Text(
                        "32h",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        "Tiempo",
                        fontSize = 10.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AchievementsPreviewCard(onNavigate: (String) -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .clickable { onNavigate("achievements") }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Logros Desbloqueados",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text("8/25 →", fontSize = 12.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
            }

            val achievements = listOf("🏆", "⭐", "📚", "💬", "🎤", "あ", "ア", "🌸")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                achievements.take(8).forEach { achievement ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .border(2.dp, Color(0xFF4CAF50).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(achievement, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun RecentActivityCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                "Actividad Reciente",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ActivityItem("✅ Completaste \"Vocabulario Básico\"", "Hace 2 horas")
                ActivityItem("🏆 Desbloqueaste \"Estudiante Dedicado\"", "Hace 5 horas")
                ActivityItem("📖 Practicaste Gramática", "Hace 1 día")
                ActivityItem("💬 Publicaste en Comunidad", "Hace 2 días")
            }
        }
    }
}

@Composable
fun ActivityItem(activity: String, time: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(activity, fontSize = 12.sp, color = Color(0xFF333333))
        Text(
            time,
            fontSize = 10.sp,
            color = Color(0xFF999999),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

// =====================================================
// PANTALLA DE COMUNIDAD (Mejorada)
// =====================================================
@Composable
fun CommunityScreen(onNavigate: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("💬 Comunidad", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(
                    "Comparte y aprende juntos",
                    fontSize = 11.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { onNavigate("newPost") },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(14.dp)
            ) {
                Text(
                    "+ Nueva Publicación",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            PostCard(
                "Maria_JP",
                "Hace 15 minutos",
                "¿Cómo puedo decir \"me gusta el anime\"?\n私はアニメが好きです\n¿Está bien así? 🤔",
                "3",
                "5"
            )
            PostCard(
                "Takeshi_sensei",
                "Hace 1 hora",
                "¡Tip del día! 💡\n❌ 寒いです\n✅ 寒いですね\nEl ね hace que suene más natural 😊",
                "8",
                "24"
            )
            PostCard(
                "Ana_2024",
                "Hace 3 horas",
                "Ayuda! No entiendo cuándo usar は vs が 😭\n¿Alguien me puede explicar con ejemplos simples?",
                "12",
                "7"
            )
            PostCard(
                "KevinLearnsJP",
                "Hace 1 día",
                "¡Logré mantener mi racha 30 días! 🎉🔥\n¿Alguien más en racha larga? Motívense!",
                "15",
                "42"
            )
        }
    }
}

@Composable
fun PostCard(author: String, time: String, content: String, replies: String, likes: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(20.dp))
                        .background(Color(0xFFE8F5E9), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        author.first().toString(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50),
                        fontSize = 16.sp
                    )
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        author,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(time, fontSize = 10.sp, color = Color(0xFF999999))
                }
            }

            Text(
                content,
                fontSize = 12.sp,
                color = Color(0xFF333333),
                lineHeight = 16.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    "💬 $replies respuestas",
                    fontSize = 10.sp,
                    color = Color(0xFF666666)
                )
                Text("👍 $likes", fontSize = 10.sp, color = Color(0xFF666666))
                Text("📌 Guardar", fontSize = 10.sp, color = Color(0xFF4CAF50))
            }
        }
    }
}

// =====================================================
// PANTALLA DE MENÚ (Mejorada)
// =====================================================
@Composable
fun MenuScreen(onNavigate: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("☰ Menú", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(
                    "Configuración y opciones",
                    fontSize = 11.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MenuSection(
                "Cuenta",
                listOf(
                    Pair("👤", "Editar Perfil") to "Nombre, avatar y más",
                    Pair("🔐", "Privacidad y Seguridad") to "Contraseña y datos"
                )
            )

            MenuSection(
                "Aprendizaje",
                listOf(
                    Pair("🎯", "Meta Diaria") to "Actualmente: 15 min/día",
                    Pair("🔔", "Recordatorios") to "Configura notificaciones",
                    Pair("📊", "Estadísticas Completas") to "Ver todo tu progreso"
                ),
                onNavigate
            )

            MenuSection(
                "Aplicación",
                listOf(
                    Pair("🌙", "Modo Oscuro") to "Apagado",
                    Pair("🔊", "Sonidos") to "Activados",
                    Pair("🌐", "Idioma de la App") to "Español"
                )
            )

            MenuSection(
                "Soporte",
                listOf(
                    Pair("❓", "Centro de Ayuda") to "FAQs y tutoriales",
                    Pair("💌", "Contactar Soporte") to "Envía tus dudas",
                    Pair("⭐", "Califica la App") to "Déjanos tu opinión"
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFFE57373), RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp))
                    .clickable { /* Cerrar sesión */ }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "🚪 Cerrar Sesión",
                    color = Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Text(
                "Kotodama v1.0.0\n© 2025 - Todos los derechos reservados",
                fontSize = 10.sp,
                color = Color(0xFF999999),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MenuSection(
    title: String,
    items: List<Pair<Pair<String, String>, String>>,
    onNavigate: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(
                title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            items.forEachIndexed { index, (iconName, description) ->
                val (icon, name) = iconName
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            when (name) {
                                "Meta Diaria" -> onNavigate("goal")
                                "Recordatorios" -> onNavigate("reminders")
                                "Estadísticas Completas" -> onNavigate("stats")
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(icon, fontSize = 22.sp, modifier = Modifier.padding(end = 12.dp))
                        Column {
                            Text(
                                name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                description,
                                fontSize = 10.sp,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                    Text("→", fontSize = 14.sp, color = Color(0xFF4CAF50))
                }
                if (index < items.size - 1) {
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

// =====================================================
// PANTALLA DE LECCIÓN (Mejorada)
// =====================================================
@Composable
fun LessonScreen(functionName: String, onBack: () -> Unit) {
    var currentQuestion by remember { mutableStateOf(1) }
    val totalQuestions = 5
    var showResults by remember { mutableStateOf(false) }

    if (showResults) {
        ResultsScreen(
            onBack = onBack,
            onContinue = {
                showResults = false
                currentQuestion = 1
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(width = 0.dp, color = Color.Transparent)
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onBack) {
                        Text(
                            "✕ Salir",
                            fontSize = 12.sp,
                            color = Color(0xFF666666),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            functionName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            "Pregunta $currentQuestion/$totalQuestions",
                            fontSize = 10.sp,
                            color = Color(0xFF666666)
                        )
                    }

                    Spacer(modifier = Modifier.width(50.dp))
                }
            }

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(8.dp)
                    .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(4.dp))
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(currentQuestion / totalQuestions.toFloat())
                        .fillMaxHeight()
                        .background(Color(0xFF4CAF50), RoundedCornerShape(4.dp))
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "¿Qué significa este kanji?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp),
                    color = Color.Black
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f)
                        .border(3.dp, Color(0xFFDDDDDD), RoundedCornerShape(12.dp))
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("水", fontSize = 80.sp)
                }

                Column(
                    modifier = Modifier.padding(top = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OptionButton("A) Fuego")
                    OptionButton("B) Agua")
                    OptionButton("C) Tierra")
                    OptionButton("D) Aire")
                }
            }

            // Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { if (currentQuestion > 1) currentQuestion-- },
                    enabled = currentQuestion > 1,
                    modifier = Modifier
                        .weight(1f)
                        .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "← Anterior",
                        color = if (currentQuestion > 1) Color.Black else Color(0xFF999999),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        if (currentQuestion < totalQuestions) {
                            currentQuestion++
                        } else {
                            showResults = true
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (currentQuestion < totalQuestions) "Verificar →" else "Terminar",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun OptionButton(text: String) {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(
            text,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
}

// =====================================================
// PANTALLA DE RESULTADOS (Mejorada)
// =====================================================
@Composable
fun ResultsScreen(onBack: () -> Unit, onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFE8F5E9), RoundedCornerShape(50.dp))
                .border(3.dp, Color(0xFF4CAF50), RoundedCornerShape(50.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("🎉", fontSize = 60.sp)
        }

        Text(
            "¡Excelente trabajo!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp),
            color = Color.Black
        )
        Text(
            "Has completado la lección de Hiragana",
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(10.dp))
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("4/5", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(
                        "Correctas",
                        fontSize = 11.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("80%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                    Text(
                        "Precisión",
                        fontSize = 11.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("+25", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
                    Text(
                        "XP Ganados",
                        fontSize = 11.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    "Continuar Aprendiendo",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Button(
                onClick = { /* Ver Respuestas */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    "Ver Respuestas",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    "Volver al Inicio",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// =====================================================
// NAVEGACIÓN INFERIOR (Mejorada)
// =====================================================
@Composable
fun BottomNavigation(
    currentScreen: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(width = 1.dp, color = Color(0xFFDDDDDD))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem("🏠", "Inicio", currentScreen == "home") { onNavigate("home") }
            NavItem("👤", "Perfil", currentScreen == "profile") { onNavigate("profile") }
            NavItem("💬", "Comunidad", currentScreen == "community") { onNavigate("community") }
            NavItem("☰", "Menú", currentScreen == "menu") { onNavigate("menu") }
        }
    }
}

@Composable
fun NavItem(icon: String, label: String, isActive: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(width = 80.dp, height = 70.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) Color(0xFFE8F5E9) else Color.Transparent
        ),
        shape = RoundedCornerShape(0.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(icon, fontSize = 22.sp)
            Text(
                label,
                fontSize = 9.sp,
                color = if (isActive) Color(0xFF4CAF50) else Color(0xFF999999),
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun Divider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFEEEEEE))
    )
}

// =====================================================
// PANTALLAS ADICIONALES (Continuación)
// =====================================================

// PANTALLA 6: LOGROS
@Composable
fun AchievementsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.border(0.dp, Color.Transparent),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("← Atrás", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🏆 Logros", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("8 de 25 Desbloqueados", fontSize = 11.sp, color = Color(0xFF666666))
                }
                Spacer(modifier = Modifier.width(50.dp))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(10.dp))
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(16.dp)
        ) {
            Column {
                Text("Progreso General", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(6.dp))
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(6.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.32f)
                            .fillMaxHeight()
                            .background(Color(0xFF4CAF50), RoundedCornerShape(6.dp))
                    )
                }

                Text("32% Completado", fontSize = 10.sp, color = Color(0xFF666666), modifier = Modifier.padding(top = 6.dp))
            }
        }

        Column(modifier = Modifier.padding(horizontal = 15.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Logros Básicos", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF666666), modifier = Modifier.padding(top = 8.dp))

            val basicAchievements = listOf(
                Triple("🏆", "Primer Paso", "Completa tu primera lección"),
                Triple("⭐", "Estudiante Dedicado", "Mantén una racha de 7 días"),
                Triple("📚", "Lector Voraz", "Estudia 100 lecciones")
            )

            basicAchievements.forEach { (icon, title, description) ->
                AchievementItemCard(icon, title, description, true)
            }

            Text("Logros de Interacción", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF666666), modifier = Modifier.padding(top = 12.dp))

            val socialAchievements = listOf(
                Triple("💬", "Socializador", "Publica 5 mensajes en comunidad"),
                Triple("🎤", "Orador Seguro", "Completa 10 lecciones de pronunciación")
            )

            socialAchievements.forEach { (icon, title, description) ->
                AchievementItemCard(icon, title, description, true)
            }

            Text("Logros de Idioma", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF666666), modifier = Modifier.padding(top = 12.dp))

            val languageAchievements = listOf(
                Triple("あ", "Maestro del Hiragana", "Aprende todos los hiragana"),
                Triple("ア", "Maestro del Katakana", "Aprende todos los katakana"),
                Triple("🌸", "Especialista de Kanji", "Domina 100 kanji")
            )

            languageAchievements.forEach { (icon, title, description) ->
                AchievementItemCard(icon, title, description, true)
            }

            Text("Bloqueados", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF666666), modifier = Modifier.padding(top = 12.dp))

            val lockedAchievements = listOf(
                Triple("🔥", "Racha de Fuego", "Alcanza racha de 30 días"),
                Triple("💎", "Coleccionista", "Desbloquea 15 logros")
            )

            lockedAchievements.forEach { (icon, title, description) ->
                AchievementItemCard(icon, title, description, false)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun AchievementItemCard(icon: String, title: String, description: String, isUnlocked: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                2.dp,
                if (isUnlocked) Color(0xFF4CAF50) else Color(0xFFDDDDDD),
                RoundedCornerShape(8.dp)
            )
            .background(
                if (isUnlocked) Color.White else Color(0xFFF5F5F5),
                RoundedCornerShape(8.dp)
            )
            .alpha(if (isUnlocked) 1f else 0.6f)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .border(
                        2.dp,
                        if (isUnlocked) Color(0xFF4CAF50) else Color(0xFFDDDDDD),
                        RoundedCornerShape(10.dp)
                    )
                    .background(
                        if (isUnlocked) Color(0xFFE8F5E9) else Color(0xFFEEEEEE),
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 30.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) Color.Black else Color(0xFF999999)
                )
                Text(
                    description,
                    fontSize = 10.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .border(
                        2.dp,
                        if (isUnlocked) Color(0xFF4CAF50) else Color(0xFFCCCCCC),
                        RoundedCornerShape(14.dp)
                    )
                    .background(
                        if (isUnlocked) Color(0xFF4CAF50) else Color.Transparent,
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isUnlocked) "✓" else "🔒",
                    fontSize = if (isUnlocked) 14.sp else 12.sp,
                    color = if (isUnlocked) Color.White else Color(0xFF999999)
                )
            }
        }
    }
}

// PANTALLA 7: NUEVA PUBLICACIÓN
@Composable
fun NewPostScreen(onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("General") }
    var tags by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var isSubmitting by remember { mutableStateOf(false) }
    var submitSuccess by remember { mutableStateOf(false) }

    fun validateForm(): Boolean {
        val newErrors = mutableMapOf<String, String>()

        if (content.isBlank()) {
            newErrors["content"] = "La publicación no puede estar vacía"
        } else if (content.length < 10) {
            newErrors["content"] = "Mínimo 10 caracteres requeridos"
        } else if (content.length > 1000) {
            newErrors["content"] = "Máximo 1000 caracteres permitidos"
        }

        if (title.isNotBlank() && title.length < 5) {
            newErrors["title"] = "El título debe tener al menos 5 caracteres"
        }

        if (tags.isNotBlank()) {
            val tagList = tags.split(",").map { it.trim() }
            if (tagList.size > 5) {
                newErrors["tags"] = "Máximo 5 etiquetas permitidas"
            }
        }

        errors = newErrors
        return newErrors.isEmpty()
    }

    fun handleSubmit() {
        if (validateForm()) {
            isSubmitting = true
            Thread.sleep(500)
            isSubmitting = false
            submitSuccess = true
        }
    }

    if (submitSuccess) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(30.dp)
                    .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(10.dp))
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .padding(30.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFE8F5E9), RoundedCornerShape(40.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", fontSize = 48.sp, color = Color(0xFF4CAF50))
                }

                Text(
                    "¡Publicación Enviada!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Text(
                    "Tu post aparecerá en la comunidad",
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Volver a Comunidad", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    val categories = listOf("General", "Vocabulario", "Gramática", "Kanji")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack, enabled = !isSubmitting) {
                    Text("✕ Cancelar", fontSize = 12.sp, color = Color(0xFF666666), fontWeight = FontWeight.Bold)
                }

                Text("Nueva Publicación", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                TextButton(
                    onClick = { handleSubmit() },
                    enabled = !isSubmitting && content.isNotBlank()
                ) {
                    Text(
                        if (isSubmitting) "..." else "Publicar",
                        fontSize = 12.sp,
                        color = if (!isSubmitting && content.isNotBlank()) Color(0xFF4CAF50) else Color(0xFF999999),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(15.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text("Título (opcional)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF666666))
                OutlinedTextField(
                    value = title,
                    onValueChange = { if (it.length <= 100) title = it },
                    placeholder = { Text("Ej. Duda sobre partículas", fontSize = 12.sp, color = Color(0xFFCCCCCC)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.5.dp,
                            if (errors.containsKey("title")) Color(0xFFE57373) else Color(0xFFDDDDDD),
                            RoundedCornerShape(8.dp)
                        )
                        .background(Color.White, RoundedCornerShape(8.dp)),
                    singleLine = true,
                    isError = errors.containsKey("title"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4CAF50),
                        unfocusedBorderColor = Color.Transparent
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (errors.containsKey("title")) {
                        Text(errors["title"] ?: "", fontSize = 9.sp, color = Color(0xFFE57373), modifier = Modifier.padding(top = 4.dp))
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                    Text("${title.length}/100", fontSize = 9.sp, color = Color(0xFF999999), modifier = Modifier.padding(top = 4.dp))
                }
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Tu Publicación", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF666666))
                    Text(" *", fontSize = 11.sp, color = Color(0xFFE57373))
                }

                OutlinedTextField(
                    value = content,
                    onValueChange = { if (it.length <= 1000) content = it },
                    placeholder = { Text("Escribe tu pregunta, consejo o experiencia...", fontSize = 12.sp, color = Color(0xFFCCCCCC)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .border(
                            1.5.dp,
                            if (errors.containsKey("content")) Color(0xFFE57373) else Color(0xFFDDDDDD),
                            RoundedCornerShape(8.dp)
                        )
                        .background(Color.White, RoundedCornerShape(8.dp)),
                    maxLines = 10,
                    isError = errors.containsKey("content"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4CAF50),
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (errors.containsKey("content")) {
                        Text(errors["content"] ?: "", fontSize = 9.sp, color = Color(0xFFE57373), fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                    Text("${content.length}/1000", fontSize = 9.sp, color = Color(0xFF999999), modifier = Modifier.padding(top = 4.dp))
                }
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Categoría", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF666666))
                    Text(" *", fontSize = 11.sp, color = Color(0xFFE57373))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        Button(
                            onClick = { selectedCategory = category },
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.5.dp,
                                    if (selectedCategory == category) Color(0xFF4CAF50) else Color(0xFFDDDDDD),
                                    RoundedCornerShape(6.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedCategory == category) Color(0xFFE8F5E9) else Color.White
                            ),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(10.dp),
                            enabled = !isSubmitting
                        ) {
                            Text(
                                category,
                                fontSize = 10.sp,
                                color = if (selectedCategory == category) Color(0xFF4CAF50) else Color(0xFF666666),
                                fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Column {
                Text("Etiquetas (opcional)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF666666))
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    placeholder = { Text("#ejemplo #ayuda", fontSize = 12.sp, color = Color(0xFFCCCCCC)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.5.dp,
                            if (errors.containsKey("tags")) Color(0xFFE57373) else Color(0xFFDDDDDD),
                            RoundedCornerShape(8.dp)
                        )
                        .background(Color.White, RoundedCornerShape(8.dp)),
                    singleLine = true,
                    isError = errors.containsKey("tags"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4CAF50),
                        unfocusedBorderColor = Color.Transparent
                    )
                )
                if (errors.containsKey("tags")) {
                    Text(errors["tags"] ?: "", fontSize = 9.sp, color = Color(0xFFE57373), modifier = Modifier.padding(top = 4.dp))
                } else {
                    Text("Separa con comas. Máximo 5", fontSize = 9.sp, color = Color(0xFF999999), modifier = Modifier.padding(top = 4.dp))
                }
            }

            if (content.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, Color(0xFFDDDDDD), RoundedCornerShape(8.dp))
                        .background(Color(0xFFFAFAFA), RoundedCornerShape(8.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("📝 Resumen", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF666666))
                        Text("• Categoría: $selectedCategory", fontSize = 10.sp, color = Color(0xFF666666))
                        Text("• Caracteres: ${content.length}", fontSize = 10.sp, color = Color(0xFF666666))
                        if (tags.isNotBlank()) {
                            Text("• Etiquetas: $tags", fontSize = 10.sp, color = Color(0xFF666666))
                        }
                    }
                }
            }
        }
    }
}

// PANTALLA 8: META DIARIA
@Composable
fun DailyGoalScreen(onBack: () -> Unit) {
    var selectedMinutes by remember { mutableStateOf("15 min") }
    val currentProgress = 0.8f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.border(0.dp, Color.Transparent),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("← Atrás", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎯 Meta Diaria", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Configura tu objetivo", fontSize = 11.sp, color = Color(0xFF666666))
                }
                Spacer(modifier = Modifier.width(50.dp))
            }
        }

        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(10.dp))
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Meta Actual", fontSize = 11.sp, color = Color(0xFF666666))
                            Text("15 minutos/día", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .border(3.dp, Color(0xFF4CAF50), RoundedCornerShape(30.dp))
                                .background(Color(0xFFE8F5E9), RoundedCornerShape(30.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("80%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Hoy: 12 de 15 min", fontSize = 11.sp, color = Color(0xFF666666))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(7.dp))
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(7.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(currentProgress)
                                .fillMaxHeight()
                                .background(Color(0xFF4CAF50), RoundedCornerShape(7.dp))
                        )
                    }
                }
            }

            Text("Selecciona una nueva meta", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val goalOptions = listOf(
                    Triple("5 min", "Rápido", "🏃"),
                    Triple("10 min", "Ligero", "🚶"),
                    Triple("15 min", "Balanceado", "⭐"),
                    Triple("30 min", "Comprometido", "💪"),
                    Triple("45 min", "Intenso", "🔥"),
                    Triple("60 min", "Experto", "🎓")
                )

                goalOptions.forEach { (time, label, icon) ->
                    Button(
                        onClick = { selectedMinutes = time },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                2.dp,
                                if (selectedMinutes == time) Color(0xFF4CAF50) else Color(0xFFDDDDDD),
                                RoundedCornerShape(8.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMinutes == time) Color(0xFFE8F5E9) else Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(icon, fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
                                Column {
                                    Text(
                                        time,
                                        fontSize = 14.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        label,
                                        fontSize = 10.sp,
                                        color = Color(0xFF666666)
                                    )
                                }
                            }

                            if (selectedMinutes == time) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Color(0xFF4CAF50), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✓", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFFFFB74D), RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFF8E1), RoundedCornerShape(8.dp))
                    .padding(14.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("💡", fontSize = 24.sp)
                    Column {
                        Text("Consejo", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6F00))
                        Text(
                            "Estudiar entre 15-30 minutos diarios es ideal para mantener consistencia y construir un hábito duradero.",
                            fontSize = 11.sp,
                            color = Color(0xFF666666),
                            modifier = Modifier.padding(top = 4.dp),
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}

// PANTALLA 9: RECORDATORIOS
@Composable
fun RemindersScreen(onBack: () -> Unit) {
    var remindersEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.border(0.dp, Color.Transparent),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("← Atrás", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔔 Recordatorios", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Configura notificaciones", fontSize = 11.sp, color = Color(0xFF666666))
                }
                Spacer(modifier = Modifier.width(50.dp))
            }
        }

        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        if (remindersEnabled) Color(0xFF4CAF50) else Color(0xFFDDDDDD),
                        RoundedCornerShape(8.dp)
                    )
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Recordatorios Diarios",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            if (remindersEnabled) "Activados" else "Desactivados",
                            fontSize = 10.sp,
                            color = if (remindersEnabled) Color(0xFF4CAF50) else Color(0xFF999999),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(28.dp)
                            .border(2.dp, Color.Black, RoundedCornerShape(14.dp))
                            .background(
                                if (remindersEnabled) Color(0xFF4CAF50) else Color(0xFFEEEEEE),
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { remindersEnabled = !remindersEnabled },
                        contentAlignment = if (remindersEnabled) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .size(22.dp)
                                .background(Color.White, RoundedCornerShape(11.dp))
                                .border(2.dp, Color.Black, RoundedCornerShape(11.dp))
                        )
                    }
                }
            }

            Text("Horarios de Estudio", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(top = 8.dp))

            ReminderTimeItem("Primera Sesión", "08:00 AM", "⏰", true)
            ReminderTimeItem("Segunda Sesión", "03:00 PM", "⏰", true)
            ReminderTimeItem("Tercera Sesión", "08:00 PM", "⏰", false)
            ReminderTimeItem("Recordatorio Nocturno", "09:00 PM", "🌙", false)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        "Frecuencia Semanal",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        "Selecciona los días para recibir notificaciones",
                        fontSize = 10.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("L", "M", "M", "J", "V", "S", "D").forEachIndexed { index, day ->
                            val isActive = index != 5
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .border(
                                        2.dp,
                                        if (isActive) Color(0xFF4CAF50) else Color(0xFFDDDDDD),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .background(
                                        if (isActive) Color(0xFFE8F5E9) else Color.White,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    day,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isActive) Color(0xFF4CAF50) else Color(0xFF999999)
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFF64B5F6), RoundedCornerShape(8.dp))
                    .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp))
                    .padding(14.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("ℹ️", fontSize = 24.sp)
                    Column {
                        Text("Información", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                        Text(
                            "Los recordatorios te ayudarán a mantener tu racha y alcanzar tus metas diarias de estudio.",
                            fontSize = 11.sp,
                            color = Color(0xFF666666),
                            modifier = Modifier.padding(top = 4.dp),
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReminderTimeItem(title: String, time: String, icon: String, isActive: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                2.dp,
                if (isActive) Color(0xFF4CAF50) else Color(0xFFDDDDDD),
                RoundedCornerShape(8.dp)
            )
            .background(
                if (isActive) Color.White else Color(0xFFF5F5F5),
                RoundedCornerShape(8.dp)
            )
            .alpha(if (isActive) 1f else 0.6f)
            .padding(14.dp)
            .clickable { }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
                Text(
                    title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) Color.Black else Color(0xFF999999)
                )
            }

            Box(
                modifier = Modifier
                    .border(
                        2.dp,
                        if (isActive) Color.Black else Color(0xFFCCCCCC),
                        RoundedCornerShape(6.dp)
                    )
                    .background(
                        if (isActive) Color(0xFFFAFAFA) else Color(0xFFEEEEEE),
                        RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    time,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) Color.Black else Color(0xFF999999)
                )
            }
        }
    }
}

/* PANTALLA 10: ESTADÍSTICAS COMPLETAS
@Composable
fun CompleteStatsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(bottom =
                "*/
