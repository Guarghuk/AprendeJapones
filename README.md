# 🇯🇵 AprendeJapones

<p align="center">
  <img src="docs/screenshots/logo.png" alt="AprendeJapones Logo" width="200"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-100%25-purple?style=for-the-badge&logo=kotlin" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose"/>
</p>

---

## 📖 Descripción

**AprendeJapones** es una aplicación móvil desarrollada en Kotlin para Android, diseñada para facilitar el aprendizaje del idioma japonés de manera interactiva y progresiva. La aplicación está pensada tanto para principiantes que dan sus primeros pasos en el japonés como para estudiantes intermedios que buscan reforzar sus conocimientos.

La aplicación ofrece un enfoque estructurado para el aprendizaje, comenzando con los alfabetos básicos (Hiragana y Katakana) hasta llegar a los Kanji más utilizados. Cada lección está diseñada con ejercicios prácticos que permiten al usuario memorizar y reconocer los caracteres de forma efectiva.

Además, AprendeJapones incluye funcionalidades de seguimiento de progreso, permitiendo al usuario visualizar su avance y motivándolo a continuar con su aprendizaje diario. La interfaz intuitiva y el diseño moderno hacen que estudiar japonés sea una experiencia agradable y accesible.

---

## ✨ Características Principales

- 🔤 **Aprendizaje de Hiragana y Katakana** - Lecciones completas para dominar los alfabetos básicos japoneses
- 📝 **Sistema de Kanji** - Base de datos con los Kanji más utilizados, incluyendo lecturas y significados
- 🎯 **Ejercicios Interactivos** - Quizzes y pruebas para reforzar el aprendizaje
- 📊 **Seguimiento de Progreso** - Estadísticas detalladas del avance del usuario
- 🔊 **Pronunciación de Audio** - Escucha la pronunciación correcta de cada carácter
- 🌙 **Modo Oscuro** - Interfaz adaptable para mayor comodidad visual
- 💾 **Almacenamiento Local** - Progreso guardado sin necesidad de conexión a internet

---

## 📱 Capturas de Pantalla

<p align="center">
  <img src="docs/screenshots/screenshot1.png" alt="Pantalla Principal" width="250"/>
  <img src="docs/screenshots/screenshot2.png" alt="Lección Hiragana" width="250"/>
  <img src="docs/screenshots/screenshot3.png" alt="Quiz" width="250"/>
</p>

<p align="center">
  <img src="docs/screenshots/screenshot4.png" alt="Progreso" width="250"/>
  <img src="docs/screenshots/screenshot5.png" alt="Kanji" width="250"/>
</p>

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Descripción |
|------------|-------------|
| **Kotlin** | Lenguaje de programación principal |
| **Android SDK** | Framework de desarrollo Android |
| **Jetpack Compose** | Toolkit moderno para UI declarativa |
| **Room Database** | Persistencia de datos local |
| **Coroutines** | Programación asíncrona |
| **Material Design 3** | Sistema de diseño de interfaz |
| **Gradle Kotlin DSL** | Sistema de construcción |

---

## 📥 Instalación

### Requisitos Previos
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17 o superior
- Android SDK 34
- Dispositivo Android con API 24+ (Android 7.0) o emulador

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Guarghuk/AprendeJapones.git
   ```

2. **Abrir en Android Studio**
   ```bash
   cd AprendeJapones
   ```
   Abre Android Studio y selecciona "Open an existing project"

3. **Sincronizar dependencias**
   
   Android Studio sincronizará automáticamente las dependencias de Gradle

4. **Ejecutar la aplicación**
   
   Conecta un dispositivo Android o inicia un emulador y presiona `Run` (▶️)

---

## 📁 Estructura del Proyecto

```
AprendeJapones/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/guarghuk/aprendejapones/
│   │   │   │   ├── data/           # Capa de datos (Room, repositorios)
│   │   │   │   ├── domain/         # Lógica de negocio y casos de uso
│   │   │   │   ├── ui/             # Componentes de interfaz (Compose)
│   │   │   │   │   ├── screens/    # Pantallas de la aplicación
│   │   │   │   │   ├── components/ # Componentes reutilizables
│   │   │   │   │   └── theme/      # Configuración de tema
│   │   │   │   └── utils/          # Utilidades y extensiones
│   │   │   ├── res/                # Recursos (strings, drawables, etc.)
│   │   │   └── AndroidManifest.xml
│   │   └── test/                   # Tests unitarios
│   └── build.gradle.kts
├── docs/
│   └── screenshots/                # Capturas de pantalla
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 💻 Ejemplos de Código

### Modelo de datos para caracteres japoneses

```kotlin
/**
 * Representa un carácter japonés (Hiragana, Katakana o Kanji).
 *
 * @property id Identificador único del carácter
 * @property character El carácter japonés
 * @property romaji Representación en romaji (alfabeto latino)
 * @property type Tipo de carácter (HIRAGANA, KATAKANA, KANJI)
 * @property meaning Significado en español (aplicable para Kanji)
 * @property strokeOrder Orden de trazos para escritura
 */
data class JapaneseCharacter(
    val id: Int,
    val character: String,
    val romaji: String,
    val type: CharacterType,
    val meaning: String? = null,
    val strokeOrder: List<Int> = emptyList()
) {
    /**
     * Verifica si el carácter es un Kanji.
     * @return true si es Kanji, false en caso contrario
     */
    fun isKanji(): Boolean = type == CharacterType.KANJI
}

enum class CharacterType {
    HIRAGANA,
    KATAKANA,
    KANJI
}
```

### ViewModel para gestionar el quiz

```kotlin
/**
 * ViewModel que gestiona la lógica del quiz de caracteres japoneses.
 * 
 * Controla el estado del quiz, incluyendo la pregunta actual,
 * las respuestas del usuario y el cálculo de puntuación.
 *
 * @param repository Repositorio para acceder a los datos de caracteres
 */
class QuizViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    /**
     * Carga una nueva pregunta aleatoria para el quiz.
     * Obtiene un carácter aleatorio y genera opciones de respuesta.
     */
    fun loadNextQuestion() {
        viewModelScope.launch {
            val characters = repository.getRandomCharacters(limit = 4)
            val correctAnswer = characters.first()
            
            _uiState.update { currentState ->
                currentState.copy(
                    currentCharacter = correctAnswer,
                    options = characters.shuffled(),
                    isAnswered = false
                )
            }
        }
    }

    /**
     * Procesa la respuesta seleccionada por el usuario.
     * 
     * @param selectedAnswer La respuesta elegida por el usuario
     * @return true si la respuesta es correcta, false en caso contrario
     */
    fun submitAnswer(selectedAnswer: JapaneseCharacter): Boolean {
        val isCorrect = selectedAnswer.id == _uiState.value.currentCharacter?.id
        
        _uiState.update { currentState ->
            currentState.copy(
                isAnswered = true,
                score = if (isCorrect) currentState.score + 1 else currentState.score,
                totalQuestions = currentState.totalQuestions + 1
            )
        }
        
        return isCorrect
    }
}
```

---

## 👤 Autores

**Guarghuk (José Enrique Ramírez Guerrero)**
**ALTMAXXUS (José Emmanuel Rodríguez Arvizu)**

- GitHub: [@Guarghuk](https://github.com/Guarghuk)

---

<p align="center">
  Hecho con ❤️ para los amantes del japonés
</p>
