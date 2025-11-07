package com.example.aprendejapones.utils

import com.example.aprendejapones.domain.model.*

/**
 * Datos mock para desarrollo
 * Simula lo que eventualmente vendrá de la base de datos o API
 */
object MockData {

    fun getMockUser() = User(
        id = "user_123",
        username = "Usuario123",
        rank = "初心者",
        level = 8,
        currentXP = 1450,
        maxXP = 2000,
        streak = 7,
        drops = 150,
        memberSince = "Enero 2025"
    )

    fun getMockDailyChallenge() = DailyChallenge(
        id = "challenge_today",
        completed = 3,
        total = 5,
        timeRemaining = "23:45:12",
        rewardXP = 50
    )

    fun getMockKitsuneMessage() = KitsuneMessage(
        message = "¡Buenos días! Hoy es perfecto para practicar."
    )

    fun getMockLessonFunctions() = listOf(
        LessonFunction("1", "💬", "Haz Frases", "Nuevas palabras"),
        LessonFunction("2", "📚", "Vocabulario", "Palabras esenciales"),
        LessonFunction("3", "あ", "Hiragana", "Sistema silábico"),
        LessonFunction("4", "ア", "Katakana", "Palabras extranjeras"),
        LessonFunction("5", "漢", "Kanji", "Caracteres japoneses"),
        LessonFunction("6", "🗣️", "Conversación", "Habla con IA"),
        LessonFunction("7", "🎤", "Pronunciación", "Escucha y repite"),
        LessonFunction("8", "📖", "Gramática", "Estructuras y partículas")
    )

    // Para ProfileScreen
    fun getMockStats() = mapOf(
        "streak" to 15,
        "lessonsCompleted" to 156,
        "totalTime" to "32h"
    )

    fun getMockAchievements() = listOf(
        Achievement("1", "🏆", "Primer Paso", "Completa tu primera lección", true),
        Achievement("2", "⭐", "Estudiante Dedicado", "Mantén una racha de 7 días", true),
        Achievement("3", "📚", "Lector Voraz", "Estudia 100 lecciones", true),
        Achievement("4", "💬", "Socializador", "Publica 5 mensajes en comunidad", true),
        Achievement("5", "🎤", "Orador Seguro", "Completa 10 lecciones de pronunciación", true),
        Achievement("6", "あ", "Maestro del Hiragana", "Aprende todos los hiragana", true),
        Achievement("7", "ア", "Maestro del Katakana", "Aprende todos los katakana", true),
        Achievement("8", "🌸", "Especialista de Kanji", "Domina 100 kanji", true),
        Achievement("9", "🔥", "Racha de Fuego", "Alcanza racha de 30 días", false),
        Achievement("10", "💎", "Coleccionista", "Desbloquea 15 logros", false)
    )

    fun getMockRecentActivity() = listOf(
        Activity("1", "✅ Completaste \"Vocabulario Básico\"", "Hace 2 horas"),
        Activity("2", "🏆 Desbloqueaste \"Estudiante Dedicado\"", "Hace 5 horas"),
        Activity("3", "📖 Practicaste Gramática", "Hace 1 día"),
        Activity("4", "💬 Publicaste en Comunidad", "Hace 2 días")
    )

    // Para CommunityScreen
    fun getMockPosts() = listOf(
        Post(
            "1", "Maria_JP", "Hace 15 minutos",
            "¿Cómo puedo decir \"me gusta el anime\"?\n私はアニメが好きです\n¿Está bien así? 🤔",
            3, 5, "General"
        ),
        Post(
            "2", "Takeshi_sensei", "Hace 1 hora",
            "¡Tip del día! 💡\n❌ 寒いです\n✅ 寒いですね\nEl ね hace que suene más natural 😊",
            8, 24, "Gramática"
        ),
        Post(
            "3", "Ana_2024", "Hace 3 horas",
            "Ayuda! No entiendo cuándo usar は vs が 😭\n¿Alguien me puede explicar con ejemplos simples?",
            12, 7, "Gramática"
        ),
        Post(
            "4", "KevinLearnsJP", "Hace 1 día",
            "¡Logré mantener mi racha 30 días! 🎉🔥\n¿Alguien más en racha larga? Motívense!",
            15, 42, "General"
        )
    )
}

// Modelos adicionales necesarios
data class Achievement(
    val id: String,
    val icon: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean
)

data class Activity(
    val id: String,
    val description: String,
    val time: String
)

data class Post(
    val id: String,
    val author: String,
    val time: String,
    val content: String,
    val replies: Int,
    val likes: Int,
    val category: String
)