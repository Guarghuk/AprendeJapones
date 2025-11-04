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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotodamaApp()
        }
    }
}
//Composable principal, aqui se especifica la navegacion de la aplicacion
@Composable
fun KotodamaApp() {
    var currentScreen by remember { mutableStateOf("home") }
    var selectedFunction by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
//pantalla principal, aqui se pueden agregar nuevas pantallas.
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
            "stats" -> CompleteStatsScreen(onBack = { onNavigate("menu") })
        }

        BottomNavigation(
            currentScreen = currentScreen,
            onNavigate = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

//Pantalla de inicio
@Composable
fun HomeScreen(onSelectFunction: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 70.dp)
    ) {
        HeaderSection()
        TreeSection()
        KitsuneMessage()
        FunctionsGrid(onSelectFunction)
        DailyChallengeBox()
    }
}
//cabecera de la pantalla de inicio
@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0E0E0))
            .border(2.dp, Color(0xFF999999))
            .padding(12.dp)
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
                        .size(40.dp)
                        .background(Color(0xFF999999), RoundedCornerShape(50))
                        .border(2.dp, Color.Black, RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("K", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                }
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text("Rango", fontSize = 9.sp, color = Color(0xFF666666))
                    Text("初心者", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatBadge("🔥 7")
                StatBadge("💧 150")
                StatBadge("🔔")
            }
        }
    }
}
//estadisticas, parte de la cabecera.
@Composable
fun StatBadge(text: String) {
    Box(
        modifier = Modifier
            .border(2.dp, Color.Black)
            .padding(4.dp, 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TreeSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .border(2.dp, Color(0xFFDDDDDD))
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🌳", fontSize = 60.sp, modifier = Modifier.padding(bottom = 10.dp))
        Text("Tu Santuario Digital", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text("こんにちは！", fontSize = 13.sp, color = Color(0xFF666666))
    }
}
//mensaje diario, pantalla de inicio
@Composable
fun KitsuneMessage() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .border(2.dp, Color(0xFF999999))
            .background(Color(0xFFF9F9F9))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(35.dp)
                .border(2.dp, Color.Black)
                .background(Color.White, RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text("🦊", fontSize = 20.sp)
        }

        Column {
            Text("Kitsune-sensei", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(
                "¡Buenos días! Hoy es perfecto para practicar.",
                fontSize = 12.sp,
                color = Color(0xFF333333),
                lineHeight = 14.sp
            )
        }
    }
}
//lista de funciones y sus estilos
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
        //aqui se pueden agregar mas funciones
    )

    Column(modifier = Modifier.padding(15.dp)) {
        Text("Funciones", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp), color = Color.Black)
        //columna de las funciones
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            //muestra dos funciones y crea otra row
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


//carta de las funciones, esta funcion es llamada iterativamente.
@Composable
fun FunctionCard(icon: String, name: String, subtitle: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(110.dp)
            .border(2.dp, Color(0xFF999999)),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFAFAFA)),
        contentPadding = PaddingValues(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Text(icon, fontSize = 28.sp)
            Text(name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black, textAlign = TextAlign.Center)
            Text(subtitle, fontSize = 9.sp, color = Color(0xFF666666), textAlign = TextAlign.Center)
        }
    }
}
//Caja que contiene el reto diario.
@Composable
fun DailyChallengeBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .border(3.dp, Color.Black)
            .background(Color(0xFFE8E8E8))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Desafío Diario", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .padding(2.dp, 2.dp)
                ) {
                    Text("23:45:12", fontSize = 10.sp, color = Color.Black)
                }
            }

            Text("Completa 5 actividades", fontSize = 11.sp, modifier = Modifier.padding(top = 8.dp), color = Color.Black)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .border(2.dp, Color.Black)
                    .padding(top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight()
                        .background(Color.Black)
                )
            }

            Text("3/5 completadas • +50 精", fontSize = 10.sp, color = Color(0xFF666666), modifier = Modifier.padding(top = 6.dp))
        }
    }
}
//pantalla del perfil
@Composable
fun ProfileScreen(onNavigate: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD0D0D0))
                .border(2.dp, Color(0xFF999999))
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Tu Perfil", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text("Logros y Progreso", fontSize = 11.sp, color = Color(0xFF555555))
            }
        }

        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            UserInfoCard()
            StatsCard()
            Button(
                onClick = { onNavigate("achievements") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Logros")
            }
            AchievementsCard()
            RecentActivityCard()
        }
    }
}
//informacion del usuario
@Composable
fun UserInfoCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF999999))
            .background(Color(0xFFFAFAFA))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(2.dp, Color.Black)
                        .background(Color(0xFF999999), RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("K", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text("Usuario123", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Rango: 初心者", fontSize = 11.sp, color = Color(0xFF666666))
                    Text("Miembro desde: Enero 2025", fontSize = 10.sp, color = Color(0xFF999999))
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Text("Experiencia Total", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Nivel 8", fontSize = 11.sp, color = Color.Black)
                Text("1,450 / 2,000 XP", fontSize = 11.sp, color = Color.Black)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .border(2.dp, Color.Black)
                    .padding(top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .fillMaxHeight()
                        .background(Color.Black)
                )
            }
        }
    }
}
//Carta en donde se muestran las estadisticas generales de el usuario
//falta boton para acceder a la pantalla de estadisticas
@Composable
fun StatsCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF999999))
            .background(Color(0xFFFAFAFA))
            .padding(12.dp)
    ) {
        Column {
            Text("Estadísticas Generales", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔥 15", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Racha de días", fontSize = 11.sp, color = Color(0xFF666666))
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("156", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Lecciones", fontSize = 11.sp, color = Color(0xFF666666))
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("32h", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Tiempo total", fontSize = 11.sp, color = Color(0xFF666666))
                }
            }
        }
    }
}
//Carta de cada uno de los logros.
@Composable
fun AchievementsCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF999999))
            .background(Color(0xFFFAFAFA))
            .padding(12.dp)
    ) {
        Column {
            Text("Logros Desbloqueados (8/25)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 10.dp))

            val achievements = listOf(
                "🏆", "⭐", "📚", "💬",
                "🎤", "あ", "ア", "🌸",
                "🔒", "🔒", "🔒", "🔒"
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (i in achievements.indices step 4) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (j in 0..3) {
                            if (i + j < achievements.size) {
                                val isLocked = achievements[i + j] == "🔒"
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .border(
                                            2.dp,
                                            if (isLocked) Color(0xFF999999) else Color.Black
                                        )
                                        .background(
                                            if (isLocked) Color(0xFFEEEEEE) else Color(
                                                0xFFFAFAFA
                                            )
                                        )
                                        .alpha(if (isLocked) 0.3f else 1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(achievements[i + j], fontSize = 20.sp)
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
//Actividad reciente
@Composable
fun RecentActivityCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF999999))
            .background(Color(0xFFFAFAFA))
            .padding(12.dp)
    ) {
        Column {
            Text("Actividad Reciente", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
    Column(modifier = Modifier.padding(8.dp, 0.dp)) {
        Text(activity, fontSize = 12.sp, color = Color(0xFF333333))
        Text(time, fontSize = 10.sp, color = Color(0xFF999999))
    }
}
//Pantalla de comunidad
@Composable
fun CommunityScreen(onNavigate: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD0D0D0))
                .border(2.dp, Color(0xFF999999))
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Comunidad", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text("Comparte y aprende juntos", fontSize = 11.sp, color = Color(0xFF555555))
            }
        }

        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(12.dp)
                    .clickable { onNavigate("newPost") },
                contentAlignment = Alignment.Center
            ) {
                Text("+ Nueva Publicación", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            //publicaciones
            PostCard("Maria_JP", "Hace 15 minutos", "¿Cómo puedo decir \"me gusta el anime\"?\n私はアニメが好きです\n¿Está bien así? 🤔", "3", "5")
            PostCard("Takeshi_sensei", "Hace 1 hora", "¡Tip del día! 💡\n❌ 寒いです\n✅ 寒いですね\nEl ね hace que suene más natural 😊", "8", "24")
            PostCard("Ana_2024", "Hace 3 horas", "Ayuda! No entiendo cuándo usar は vs が 😭\n¿Alguien me puede explicar con ejemplos simples?", "12", "7")
            PostCard("KevinLearnsJP", "Hace 1 día", "¡Logré mantener mi racha 30 días! 🎉🔥\n¿Alguien más en racha larga? Motívense!", "15", "42")
        }
    }
}
//carta por publicacion
@Composable
fun PostCard(author: String, time: String, content: String, replies: String, likes: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF999999))
            .background(Color(0xFFFAFAFA))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .border(2.dp, Color.Black)
                        .background(Color(0xFFDDDDDD), RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(author.first().toString(), fontWeight = FontWeight.Bold, color = Color.Black)
                }
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(author, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(time, fontSize = 10.sp, color = Color(0xFF666666))
                }
            }

            Text(content, fontSize = 11.sp, color = Color(0xFF333333), lineHeight = 14.sp, modifier = Modifier.padding(bottom = 8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 8.dp)) {
                Text("💬 $replies respuestas", fontSize = 10.sp, color = Color(0xFF666666))
                Text("👍 $likes", fontSize = 10.sp, color = Color(0xFF666666))
                Text("📌 Guardar", fontSize = 10.sp, color = Color(0xFF666666))
            }
        }
    }
}
//Pantalla de menu (posible cambio a menu hamburguesa)
@Composable
fun MenuScreen(onNavigate: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD0D0D0))
                .border(2.dp, Color(0xFF999999))
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Menú", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text("Configuración y opciones", fontSize = 11.sp, color = Color(0xFF555555))
            }
        }

        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MenuSection("Cuenta", listOf(
                Pair("👤", "Editar Perfil") to "Nombre, avatar y más",
                Pair("🔐", "Privacidad y Seguridad") to "Contraseña y datos"
            ))

            MenuSection("Aprendizaje", listOf(
                Pair("🎯", "Meta Diaria") to "Actualmente: 15 min/día",
                Pair("🔔", "Recordatorios") to "Configura notificaciones",
                Pair("📊", "Estadísticas Completas") to "Ver todo tu progreso"
            ), onNavigate)

            MenuSection("Aplicación", listOf(
                Pair("🌙", "Modo Oscuro") to "Apagado",
                Pair("🔊", "Sonidos") to "Activados",
                Pair("🌐", "Idioma de la App") to "Español"
            ))

            MenuSection("Soporte", listOf(
                Pair("❓", "Centro de Ayuda") to "FAQs y tutoriales",
                Pair("💌", "Contactar Soporte") to "Envía tus dudas",
                Pair("⭐", "Califica la App") to "Déjanos tu opinión"
            ))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .border(2.dp, Color(0xFF999999))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("🚪 Cerrar Sesión", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Text(
                "Kotodama v1.0.0\n© 2025 - Todos los derechos reservados",
                fontSize = 10.sp,
                color = Color(0xFF999999),
                modifier = Modifier.padding(top = 20.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
//secciones del menu, aqui se usa el onNavigate para redirigir al usuario
@Composable
fun MenuSection(title: String, items: List<Pair<Pair<String, String>, String>>, onNavigate: (String) -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF999999))
            .background(Color(0xFFFAFAFA))
            .padding(12.dp)
    ) {
        Column {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))
            items.forEachIndexed { index, (iconName, description) ->
                val (icon, name) = iconName
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (index < items.size - 1) 8.dp else 0.dp)
                        .clickable {
                            when (name) {
                                "Meta Diaria" -> onNavigate("goal")
                                "Recordatorios" -> onNavigate("reminders")
                                "Estadísticas Completas" -> onNavigate("stats")
                                // ...agrega más si quieres conectar otras pantallas
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(icon, fontSize = 20.sp, modifier = Modifier.padding(end = 10.dp))
                        Column {
                            Text(name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(description, fontSize = 10.sp, color = Color(0xFF666666))
                        }
                    }
                    Text("→", fontSize = 12.sp, color = Color.Black)
                }
            }
        }
    }
}
//pantalla de leccion, falta mucha funcionalidad, se puede acceder
//a la pantalla de leccion finalizada dandole click varias veces a continuar.
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
                .background(Color.White)
        ) {
            // Lesson Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD0D0D0))
                    .border(2.dp, Color(0xFF999999))
                    .padding(12.dp),
                //contentAlignment = Alignment.SpaceBetween
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .border(0.dp, Color.Transparent),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("✕ Salir", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Text("Pregunta $currentQuestion/$totalQuestions", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.width(0.dp))
            }

            // Lesson Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("¿Qué significa este kanji?", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 20.dp), color = Color.Black)

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f)
                        .border(3.dp, Color(0xFF999999))
                        .padding(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("水", fontSize = 80.sp)
                }

                Column(modifier = Modifier.padding(top = 25.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OptionButton("A) Fuego")
                    OptionButton("B) Agua")
                    OptionButton("C) Tierra")
                    OptionButton("D) Aire")
                }
            }

            // Lesson Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .border(2.dp, Color(0xFF999999))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)  // ← Agregado espaciado
            ) {
                Button(
                    onClick = { if (currentQuestion > 1) currentQuestion-- },
                    enabled = currentQuestion > 1,
                    modifier = Modifier
                        .weight(1f)
                        .border(2.dp, Color.Black),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text("← Anterior", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                        .border(2.dp, Color.Black),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(0.dp)
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
//funcion para los botones de la leccion
@Composable
fun OptionButton(text: String) {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF999999))
            .background(Color.White),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp),
        contentPadding = PaddingValues(15.dp)
    ) {
        Text(text, color = Color.Black, fontSize = 14.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start)
    }
}
//pantalla de resultados por leccion
@Composable
fun ResultsScreen(onBack: () -> Unit, onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎉", fontSize = 80.sp, modifier = Modifier.padding(bottom = 20.dp))
        Text("¡Excelente trabajo!", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp), color = Color.Black)
        Text("Has completado la lección de Hiragana", fontSize = 14.sp, color = Color(0xFF666666), modifier = Modifier.padding(bottom = 30.dp), textAlign = TextAlign.Center)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(0xFF999999))
                .background(Color(0xFFFAFAFA))
                .padding(20.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("4/5", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Correctas", fontSize = 11.sp, color = Color(0xFF666666))
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("80%", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Precisión", fontSize = 11.sp, color = Color(0xFF666666))
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("+25", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("XP Ganados", fontSize = 11.sp, color = Color(0xFF666666))
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Black)
                    .background(Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(0.dp),
                contentPadding = PaddingValues(15.dp)
            ) {
                Text("Continuar Aprendiendo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Button(
                onClick = { /* Ver Respuestas */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(0.dp),
                contentPadding = PaddingValues(15.dp)
            ) {
                Text("Ver Respuestas", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(0.dp),
                contentPadding = PaddingValues(15.dp)
            ) {
                Text("Volver al Inicio", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
//navegacion global
@Composable
fun BottomNavigation(
    currentScreen: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .border(2.dp, Color(0xFF999999))
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
//Item de navegacion
@Composable
fun NavItem(icon: String, label: String, isActive: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(70.dp)
            .background(if (isActive) Color(0xFFCCCCCC) else Color.Transparent),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) Color(0xFFCCCCCC) else Color.Transparent
        ),
        shape = RoundedCornerShape(0.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(icon, fontSize = 20.sp)
            Text(
                label,
                fontSize = 9.sp,
                color = if (isActive) Color.Black else Color(0xFF666666),
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
//division del navegador
@Composable
fun Divider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFDDDDDD))
    )
}

// Main function to preview
@Composable
@Preview(showBackground = true)
fun KotodamaPreview() {
    KotodamaApp()
}

// PANTALLA 6: LOGROS

@Composable

fun AchievementsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD0D0D0))
                .border(2.dp, Color(0xFF999999))
                .padding(15.dp)
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
                    Text("Logros", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("8/25 Desbloqueados", fontSize = 11.sp, color = Color(0xFF555555))
                }
                Spacer(modifier = Modifier.width(50.dp))
            }
        }

        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val achievementList = listOf(
                Triple("🏆", "Primer Paso", "Completa tu primera lección"),
                Triple("⭐", "Estudiante Dedicado", "Mantén una racha de 7 días"),
                Triple("📚", "Lector Voraz", "Estudia 100 lecciones"),
                Triple("💬", "Socializador", "Publica 5 mensajes en comunidad"),
                Triple("🎤", "Orador Seguro", "Completa 10 lecciones de pronunciación"),
                Triple("あ", "Maestro del Hiragana", "Aprende todos los hiragana"),
                Triple("ア", "Maestro del Katakana", "Aprende todos los katakana"),
                Triple("🌸", "Especialista de Kanji", "Domina 100 kanji"),
                Triple("🔥", "Racha de Fuego", "Alcanza racha de 30 días"),
                Triple("💎", "Coleccionista", "Desbloquea 15 logros")
            )

            achievementList.forEach { (icon, title, description) ->
                val isUnlocked = achievementList.indexOf(Triple(icon, title, description)) < 8
                AchievementItemCard(icon, title, description, isUnlocked)
            }
        }
    }
}

@Composable
fun AchievementItemCard(icon: String, title: String, description: String, isUnlocked: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, if (isUnlocked) Color.Black else Color(0xFF999999))
            .background(if (isUnlocked) Color(0xFFFAFAFA) else Color(0xFFEEEEEE))
            .alpha(if (isUnlocked) 1f else 0.5f)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .border(2.dp, if (isUnlocked) Color.Black else Color(0xFF999999))
                    .background(Color(0xFFEEEEEE)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 28.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(description, fontSize = 10.sp, color = Color(0xFF666666))
            }
            Text(if (isUnlocked) "✓" else "🔒", fontSize = 16.sp)
        }
    }
}

// PANTALLA 7: NUEVA PUBLICACIÓN
@Composable
fun NewPostScreen(onBack: () -> Unit) {
    // Estados para los campos del formulario
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("General") }
    var tags by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var isSubmitting by remember { mutableStateOf(false) }
    var submitSuccess by remember { mutableStateOf(false) }

    // Función de validación
    fun validateForm(): Boolean {
        val newErrors = mutableMapOf<String, String>()

        // Validar contenido principal
        if (content.isBlank()) {
            newErrors["content"] = "La publicación no puede estar vacía"
        } else if (content.length < 10) {
            newErrors["content"] = "Mínimo 10 caracteres requeridos"
        } else if (content.length > 1000) {
            newErrors["content"] = "Máximo 1000 caracteres permitidos"
        }

        // Validar título si se proporciona
        if (title.isNotBlank() && title.length < 5) {
            newErrors["title"] = "El título debe tener al menos 5 caracteres"
        }

        // Validar etiquetas si se proporcionan
        if (tags.isNotBlank()) {
            val tagList = tags.split(",").map { it.trim() }
            if (tagList.size > 5) {
                newErrors["tags"] = "Máximo 5 etiquetas permitidas"
            }
        }

        errors = newErrors
        return newErrors.isEmpty()
    }

    // Función de envío
    fun handleSubmit() {
        if (validateForm()) {
            isSubmitting = true
            // Simular delay de envío (en producción sería una llamada a API)
            Thread.sleep(1000)
            isSubmitting = false
            submitSuccess = true
            // Limpiar formulario después de envío exitoso
            title = ""
            content = ""
            selectedCategory = "General"
            tags = ""
        }
    }

    // Si el envío fue exitoso, mostrar mensaje de confirmación
    if (submitSuccess) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                Text("✓", fontSize = 64.sp, color = Color(0xFF4CAF50))
                Text(
                    "¡Publicación enviada exitosamente!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    "Tu post aparecerá en la comunidad en breve",
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 8.dp)
                )
                Button(
                    onClick = onBack,
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Text("Volver a Comunidad")
                }
            }
        }
        return
    }

    val categories = listOf("General", "Vocabulario", "Gramática", "Kanji")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Encabezado (botón cancelar / título / botón publicar)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD0D0D0))
                .border(2.dp, Color(0xFF999999))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onBack,
                    enabled = !isSubmitting
                ) {
                    Text("✕ Cancelar", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }

                Text("Nueva Publicación", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                // Botón de publicar con validación
                TextButton(
                    onClick = { handleSubmit() },
                    enabled = !isSubmitting && content.isNotBlank()
                ) {
                    Text(
                        if (isSubmitting) "Enviando..." else "Publicar",
                        fontSize = 12.sp,
                        color = if (!isSubmitting && content.isNotBlank()) Color.Black else Color(0xFF999999),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Contenido del formulario
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(15.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Campo: Título
            Text("Título (opcional)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            OutlinedTextField(
                value = title,
                onValueChange = { newValue ->
                    if (newValue.length <= 100) {
                        title = newValue
                    }
                },
                placeholder = { Text("Ej. Duda sobre partículas") },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, if (errors.containsKey("title")) Color.Red else Color(0xFF999999), shape = RoundedCornerShape(0.dp)),
                singleLine = true,
                isError = errors.containsKey("title")
            )
            if (errors.containsKey("title")) {
                Text(errors["title"] ?: "", fontSize = 10.sp, color = Color.Red)
            }
            Text("${title.length}/100", fontSize = 9.sp, color = Color(0xFF999999))

            // Campo: Contenido principal
            Text("Tu Publicación *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            OutlinedTextField(
                value = content,
                onValueChange = { newValue ->
                    if (newValue.length <= 1000) {
                        content = newValue
                    }
                },
                placeholder = { Text("Escribe tu pregunta, consejo o experiencia...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(
                        2.dp,
                        if (errors.containsKey("content")) Color.Red else Color(0xFF999999),
                        shape = RoundedCornerShape(0.dp)
                    ),
                maxLines = 10,
                isError = errors.containsKey("content")
            )
            if (errors.containsKey("content")) {
                Text(errors["content"] ?: "", fontSize = 10.sp, color = Color.Red, fontWeight = FontWeight.Bold)
            }
            Text("${content.length}/1000", fontSize = 9.sp, color = Color(0xFF999999))

            // Campo: Categoría
            Text("Categoría *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    Button(
                        onClick = { selectedCategory = category },
                        modifier = Modifier
                            .weight(1f)
                            .border(2.dp, if (selectedCategory == category) Color.Black else Color(0xFF999999)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedCategory == category) Color.Black else Color.White
                        ),
                        shape = RoundedCornerShape(0.dp),
                        contentPadding = PaddingValues(8.dp),
                        enabled = !isSubmitting
                    ) {
                        Text(
                            category,
                            fontSize = 10.sp,
                            color = if (selectedCategory == category) Color.White else Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Campo: Etiquetas
            Text("Etiquetas (opcional)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                placeholder = { Text("#ejemplo #ayuda") },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, if (errors.containsKey("tags")) Color.Red else Color(0xFF999999), shape = RoundedCornerShape(0.dp)),
                singleLine = true,
                isError = errors.containsKey("tags")
            )
            if (errors.containsKey("tags")) {
                Text(errors["tags"] ?: "", fontSize = 10.sp, color = Color.Red)
            }
            Text("Separa etiquetas con comas. Máximo 5", fontSize = 9.sp, color = Color(0xFF999999))

            // Resumen del formulario
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFF999999))
                    .background(Color(0xFFFAFAFA))
                    .padding(12.dp)
            ) {
                Column {
                    Text("Resumen de tu Publicación", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))
                    Text("Categoría: $selectedCategory", fontSize = 10.sp, color = Color(0xFF666666))
                    Text("Caracteres: ${content.length}", fontSize = 10.sp, color = Color(0xFF666666))
                    if (tags.isNotBlank()) {
                        Text("Etiquetas: $tags", fontSize = 10.sp, color = Color(0xFF666666))
                    }
                }
            }
        }
    }
}
// PANTALLA 8: META DIARIA
@Composable
fun DailyGoalScreen(onBack: () -> Unit) {
    var selectedMinutes by remember { mutableStateOf("15") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD0D0D0))
                .border(2.dp, Color(0xFF999999))
                .padding(15.dp)
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
                    Text("Meta Diaria", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Configura tu objetivo", fontSize = 11.sp, color = Color(0xFF555555))
                }
                Spacer(modifier = Modifier.width(50.dp))
            }
        }

        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFF999999))
                    .background(Color(0xFFFAFAFA))
                    .padding(15.dp)
            ) {
                Column {
                    Text("Meta Actual: 15 minutos/día", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Estudiaste 12 de 15 min hoy", fontSize = 11.sp, color = Color(0xFF666666), modifier = Modifier.padding(top = 8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .border(2.dp, Color.Black)
                            .padding(top = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight()
                                .background(Color.Black)
                        )
                    }
                }
            }

            Text("Selecciona una nueva meta", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val goalOptions = listOf("5 min", "10 min", "15 min", "30 min", "45 min", "60 min")
                goalOptions.forEach { goal ->
                    Button(
                        onClick = { selectedMinutes = goal },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, if (selectedMinutes == goal) Color.Black else Color(0xFF999999)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMinutes == goal) Color(0xFFCCCCCC) else Color.White
                        ),
                        shape = RoundedCornerShape(0.dp),
                        contentPadding = PaddingValues(15.dp)
                    ) {
                        Text(goal, fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFF999999))
                    .background(Color(0xFFFAFAFA))
                    .padding(12.dp)
            ) {
                Column {
                    Text("💡 Consejo", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(
                        "Estudiar entre 15-30 minutos diarios es lo ideal para mantener consistencia y construir hábito.",
                        fontSize = 10.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(top = 6.dp),
                        lineHeight = 12.sp
                    )
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
            .verticalScroll(rememberScrollState())
            .padding(bottom = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD0D0D0))
                .border(2.dp, Color(0xFF999999))
                .padding(15.dp)
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
                    Text("Recordatorios", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Configura notificaciones", fontSize = 11.sp, color = Color(0xFF555555))
                }
                Spacer(modifier = Modifier.width(50.dp))
            }
        }

        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ReminderToggleItem("🔔 Recordatorios Activados", remindersEnabled) { remindersEnabled = it }

            ReminderTimeItem("⏰ Primera Sesión", "08:00 AM", true)
            ReminderTimeItem("⏰ Segunda Sesión", "03:00 PM", true)
            ReminderTimeItem("⏰ Tercera Sesión", "08:00 PM", false)
            ReminderTimeItem("🌙 Recordatorio Nocturno", "09:00 PM", false)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFF999999))
                    .background(Color(0xFFFAFAFA))
                    .padding(12.dp)
            ) {
                Column {
                    Text("Frecuencia de Recordatorios", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Recibirás notificaciones solo en los días que establezcas", fontSize = 10.sp, color = Color(0xFF666666), modifier = Modifier.padding(top = 6.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("L", "M", "M", "J", "V", "S", "D").forEach { day ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .border(2.dp, if (day != "S") Color.Black else Color(0xFF999999))
                                    .background(if (day != "S") Color(0xFFCCCCCC) else Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(day, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReminderToggleItem(title: String, isEnabled: Boolean, onToggle: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF999999))
            .background(Color(0xFFFAFAFA))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(24.dp)
                    .border(2.dp, Color.Black)
                    .background(if (isEnabled) Color.Black else Color.White),
                contentAlignment = if (isEnabled) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(if (isEnabled) Color.White else Color.Black)
                )
            }
        }
    }
}

@Composable
fun ReminderTimeItem(title: String, time: String, isActive: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, if (isActive) Color.Black else Color(0xFF999999))
            .background(if (isActive) Color(0xFFFAFAFA) else Color(0xFFEEEEEE))
            .alpha(if (isActive) 1f else 0.6f)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Box(
                modifier = Modifier
                    .border(2.dp, Color.Black)
                    .padding(6.dp, 3.dp)
            ) {
                Text(time, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

// PANTALLA 10: ESTADÍSTICAS COMPLETAS
@Composable
fun CompleteStatsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD0D0D0))
                .border(2.dp, Color(0xFF999999))
                .padding(15.dp)
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
                    Text("Estadísticas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Tu Progreso Completo", fontSize = 11.sp, color = Color(0xFF555555))
                }
                Spacer(modifier = Modifier.width(50.dp))
            }
        }

        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCardLarge("Racha Actual", "🔥", "15 días", "Sigue aprendiendo")
            StatCardLarge("Total de Lecciones", "📚", "156", "Completadas")
            StatCardLarge("Tiempo Total", "⏱️", "32 horas", "Dedicadas")
            StatCardLarge("Días Activos", "📅", "45 días", "Este mes")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFF999999))
                    .background(Color(0xFFFAFAFA))
                    .padding(12.dp)
            ) {
                Column {
                    Text("Desglose por Tema", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 12.dp))

                    StatItemRow("Vocabulario", "45 lecciones", 45)
                    Spacer(modifier = Modifier.height(8.dp))
                    StatItemRow("Hiragana", "30 lecciones", 100)
                    Spacer(modifier = Modifier.height(8.dp))
                    StatItemRow("Kanji", "50 lecciones", 60)
                    Spacer(modifier = Modifier.height(8.dp))
                    StatItemRow("Gramática", "20 lecciones", 35)
                    Spacer(modifier = Modifier.height(8.dp))
                    StatItemRow("Pronunciación", "11 lecciones", 25)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFF999999))
                    .background(Color(0xFFFAFAFA))
                    .padding(12.dp)
            ) {
                Column {
                    Text("Actividad Esta Semana", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            Pair("L", "4h"),
                            Pair("M", "3h"),
                            Pair("M", "5h"),
                            Pair("J", "2h"),
                            Pair("V", "6h"),
                            Pair("S", "3h"),
                            Pair("D", "4h")
                        ).forEach { (day, hours) ->
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .border(2.dp, Color.Black)
                                    .padding(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(day, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                Text(hours, fontSize = 10.sp, color = Color(0xFF666666), modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCardLarge(title: String, icon: String, value: String, subtitle: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF999999))
            .background(Color(0xFFFAFAFA))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(title, fontSize = 11.sp, color = Color(0xFF666666))
                Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(top = 4.dp))
                Text(subtitle, fontSize = 9.sp, color = Color(0xFF999999), modifier = Modifier.padding(top = 2.dp))
            }
            Text(icon, fontSize = 36.sp)
        }
    }
}

@Composable
fun StatItemRow(label: String, value: String, percentage: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 11.sp, color = Color.Black)
            Text(value, fontSize = 11.sp, color = Color(0xFF666666))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .border(1.dp, Color(0xFF999999))
                .padding(top = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage / 100f)
                    .fillMaxHeight()
                    .background(Color(0xFF999999))
            )
        }
    }
}