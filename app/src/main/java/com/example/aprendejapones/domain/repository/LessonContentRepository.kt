package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.presentation.screens.lesson.Question
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio para obtener contenido de lecciones
 * Por ahora con datos hardcoded, luego se puede conectar a API o base de datos local
 */
@Singleton
class LessonContentRepository @Inject constructor() {

    /**
     * Obtiene las preguntas para una lección específica
     */
    fun getQuestionsForLesson(lessonName: String): List<Question> {
        return when (lessonName) {
            "Hiragana" -> getHiraganaQuestions()
            "Katakana" -> getKatakanaQuestions()
            "Kanji" -> getKanjiQuestions()
            "Vocabulario" -> getVocabularyQuestions()
            "Gramática" -> getGrammarQuestions()
            "Haz Frases" -> getPhrasesQuestions()
            "Conversación" -> getConversationQuestions()
            "Pronunciación" -> getPronunciationQuestions()
            else -> getDefaultQuestions()
        }
    }

    private fun getHiraganaQuestions(): List<Question> {
        return listOf(
            Question(
                id = "h1",
                text = "¿Cómo se lee este hiragana?",
                content = "あ",
                options = listOf("A) a", "B) i", "C) u", "D) e"),
                correctAnswer = "A) a"
            ),
            Question(
                id = "h2",
                text = "¿Cómo se lee este hiragana?",
                content = "い",
                options = listOf("A) a", "B) i", "C) u", "D) e"),
                correctAnswer = "B) i"
            ),
            Question(
                id = "h3",
                text = "¿Cómo se lee este hiragana?",
                content = "う",
                options = listOf("A) a", "B) i", "C) u", "D) e"),
                correctAnswer = "C) u"
            ),
            Question(
                id = "h4",
                text = "¿Cómo se lee este hiragana?",
                content = "え",
                options = listOf("A) a", "B) i", "C) u", "D) e"),
                correctAnswer = "D) e"
            ),
            Question(
                id = "h5",
                text = "¿Cómo se lee este hiragana?",
                content = "お",
                options = listOf("A) o", "B) ka", "C) ki", "D) ku"),
                correctAnswer = "A) o"
            ),
            Question(
                id = "h6",
                text = "¿Cómo se lee este hiragana?",
                content = "か",
                options = listOf("A) o", "B) ka", "C) ki", "D) ku"),
                correctAnswer = "B) ka"
            ),
            Question(
                id = "h7",
                text = "¿Cómo se lee este hiragana?",
                content = "き",
                options = listOf("A) o", "B) ka", "C) ki", "D) ku"),
                correctAnswer = "C) ki"
            ),
            Question(
                id = "h8",
                text = "¿Cómo se lee este hiragana?",
                content = "く",
                options = listOf("A) o", "B) ka", "C) ki", "D) ku"),
                correctAnswer = "D) ku"
            ),
            Question(
                id = "h9",
                text = "¿Cómo se lee este hiragana?",
                content = "け",
                options = listOf("A) ke", "B) ko", "C) sa", "D) shi"),
                correctAnswer = "A) ke"
            ),
            Question(
                id = "h10",
                text = "¿Cómo se lee este hiragana?",
                content = "こ",
                options = listOf("A) ke", "B) ko", "C) sa", "D) shi"),
                correctAnswer = "B) ko"
            )
        )
    }

    private fun getKatakanaQuestions(): List<Question> {
        return listOf(
            Question(
                id = "k1",
                text = "¿Cómo se lee este katakana?",
                content = "ア",
                options = listOf("A) a", "B) i", "C) u", "D) e"),
                correctAnswer = "A) a"
            ),
            Question(
                id = "k2",
                text = "¿Cómo se lee este katakana?",
                content = "イ",
                options = listOf("A) a", "B) i", "C) u", "D) e"),
                correctAnswer = "B) i"
            ),
            Question(
                id = "k3",
                text = "¿Cómo se lee este katakana?",
                content = "ウ",
                options = listOf("A) a", "B) i", "C) u", "D) e"),
                correctAnswer = "C) u"
            ),
            Question(
                id = "k4",
                text = "¿Cómo se escribe 'coffee' en katakana?",
                content = "コーヒー",
                options = listOf("A) koohii", "B) kohii", "C) koohi", "D) coffee"),
                correctAnswer = "A) koohii"
            ),
            Question(
                id = "k5",
                text = "¿Qué palabra es esta en katakana?",
                content = "テレビ",
                options = listOf("A) terefon", "B) terebi", "C) telefon", "D) camera"),
                correctAnswer = "B) terebi"
            )
        )
    }

    private fun getKanjiQuestions(): List<Question> {
        return listOf(
            Question(
                id = "kj1",
                text = "¿Qué significa este kanji?",
                content = "水",
                options = listOf("A) Fuego", "B) Agua", "C) Tierra", "D) Aire"),
                correctAnswer = "B) Agua"
            ),
            Question(
                id = "kj2",
                text = "¿Qué significa este kanji?",
                content = "火",
                options = listOf("A) Fuego", "B) Agua", "C) Tierra", "D) Viento"),
                correctAnswer = "A) Fuego"
            ),
            Question(
                id = "kj3",
                text = "¿Qué significa este kanji?",
                content = "木",
                options = listOf("A) Metal", "B) Árbol", "C) Piedra", "D) Tierra"),
                correctAnswer = "B) Árbol"
            ),
            Question(
                id = "kj4",
                text = "¿Qué significa este kanji?",
                content = "日",
                options = listOf("A) Luna", "B) Estrella", "C) Sol/Día", "D) Noche"),
                correctAnswer = "C) Sol/Día"
            ),
            Question(
                id = "kj5",
                text = "¿Qué significa este kanji?",
                content = "月",
                options = listOf("A) Luna/Mes", "B) Sol", "C) Año", "D) Día"),
                correctAnswer = "A) Luna/Mes"
            )
        )
    }

    private fun getVocabularyQuestions(): List<Question> {
        return listOf(
            Question(
                id = "v1",
                text = "¿Cómo se dice 'gracias' en japonés?",
                content = "?",
                options = listOf("A) Konnichiwa", "B) Arigatou", "C) Sayonara", "D) Ohayou"),
                correctAnswer = "B) Arigatou"
            ),
            Question(
                id = "v2",
                text = "¿Cómo se dice 'buenos días'?",
                content = "?",
                options = listOf("A) Konbanwa", "B) Oyasumi", "C) Ohayou", "D) Konnichiwa"),
                correctAnswer = "C) Ohayou"
            ),
            Question(
                id = "v3",
                text = "¿Qué significa 'Sayonara'?",
                content = "さようなら",
                options = listOf("A) Hola", "B) Adiós", "C) Gracias", "D) Perdón"),
                correctAnswer = "B) Adiós"
            ),
            Question(
                id = "v4",
                text = "¿Cómo se dice 'sí'?",
                content = "?",
                options = listOf("A) Hai", "B) Iie", "C) Demo", "D) Sou"),
                correctAnswer = "A) Hai"
            ),
            Question(
                id = "v5",
                text = "¿Qué significa 'Sumimasen'?",
                content = "すみません",
                options = listOf("A) Gracias", "B) Perdón/Disculpe", "C) Adiós", "D) Por favor"),
                correctAnswer = "B) Perdón/Disculpe"
            )
        )
    }

    private fun getGrammarQuestions(): List<Question> {
        return listOf(
            Question(
                id = "g1",
                text = "¿Cuál es la partícula de sujeto?",
                content = "?",
                options = listOf("A) は (wa)", "B) が (ga)", "C) を (wo)", "D) に (ni)"),
                correctAnswer = "B) が (ga)"
            ),
            Question(
                id = "g2",
                text = "¿Cuál es la partícula de tópico?",
                content = "?",
                options = listOf("A) は (wa)", "B) が (ga)", "C) を (wo)", "D) の (no)"),
                correctAnswer = "A) は (wa)"
            ),
            Question(
                id = "g3",
                text = "¿Cuál es la partícula de objeto directo?",
                content = "?",
                options = listOf("A) は (wa)", "B) が (ga)", "C) を (wo)", "D) に (ni)"),
                correctAnswer = "C) を (wo)"
            ),
            Question(
                id = "g4",
                text = "¿Cómo se forma el presente negativo de 'taberu' (comer)?",
                content = "食べる",
                options = listOf(
                    "A) tabenai",
                    "B) tabemasen",
                    "C) tabenakatta",
                    "D) tabemasen deshita"
                ),
                correctAnswer = "A) tabenai"
            ),
            Question(
                id = "g5",
                text = "¿Qué significa 'desu'?",
                content = "です",
                options = listOf(
                    "A) Verbo 'ser/estar' formal",
                    "B) Partícula",
                    "C) Pregunta",
                    "D) Negación"
                ),
                correctAnswer = "A) Verbo 'ser/estar' formal"
            )
        )
    }

    private fun getPhrasesQuestions(): List<Question> {
        return listOf(
            Question(
                id = "p1",
                text = "¿Cómo se dice 'Me llamo Juan'?",
                content = "?",
                options = listOf(
                    "A) Watashi wa Juan desu",
                    "B) Juan wa watashi desu",
                    "C) Desu wa Juan watashi",
                    "D) Juan desu watashi wa"
                ),
                correctAnswer = "A) Watashi wa Juan desu"
            ),
            Question(
                id = "p2",
                text = "¿Cómo se dice 'Me gusta el sushi'?",
                content = "?",
                options = listOf(
                    "A) Sushi ga suki desu",
                    "B) Watashi wa sushi desu",
                    "C) Sushi wa suki desu",
                    "D) Suki wa sushi desu"
                ),
                correctAnswer = "A) Sushi ga suki desu"
            ),
            Question(
                id = "p3",
                text = "Completa: '_____ es estudiante'",
                content = "Watashi ___ gakusei desu",
                options = listOf("A) wa", "B) ga", "C) wo", "D) ni"),
                correctAnswer = "A) wa"
            )
        )
    }

    private fun getConversationQuestions(): List<Question> {
        return listOf(
            Question(
                id = "c1",
                text = "Responde a: 'Ogenki desu ka?' (¿Cómo estás?)",
                content = "お元気ですか？",
                options = listOf(
                    "A) Genki desu (Estoy bien)",
                    "B) Arigatou (Gracias)",
                    "C) Sumimasen (Perdón)",
                    "D) Sayonara (Adiós)"
                ),
                correctAnswer = "A) Genki desu (Estoy bien)"
            ),
            Question(
                id = "c2",
                text = "¿Cómo preguntas 'Cuánto cuesta?'",
                content = "?",
                options = listOf(
                    "A) Ikura desu ka?",
                    "B) Nan desu ka?",
                    "C) Doko desu ka?",
                    "D) Dare desu ka?"
                ),
                correctAnswer = "A) Ikura desu ka?"
            )
        )
    }

    private fun getPronunciationQuestions(): List<Question> {
        return listOf(
            Question(
                id = "pr1",
                text = "¿Cómo se pronuncia correctamente?",
                content = "りょうり (ryouri)",
                options = listOf(
                    "A) rio-ri",
                    "B) ryo-u-ri",
                    "C) rio-uri",
                    "D) ryo-ri (largo)"
                ),
                correctAnswer = "B) ryo-u-ri"
            ),
            Question(
                id = "pr2",
                text = "¿Cuál vocal es larga en esta palabra?",
                content = "おかあさん (okaasan)",
                options = listOf(
                    "A) Primera 'o'",
                    "B) 'a' del medio",
                    "C) Última 'a'",
                    "D) Ninguna"
                ),
                correctAnswer = "B) 'a' del medio"
            )
        )
    }

    private fun getDefaultQuestions(): List<Question> {
        return listOf(
            Question(
                id = "d1",
                text = "¿Listo para aprender?",
                content = "🎌",
                options = listOf("A) ¡Sí!", "B) Tal vez", "C) No estoy seguro", "D) Más tarde"),
                correctAnswer = "A) ¡Sí!"
            )
        )
    }
}