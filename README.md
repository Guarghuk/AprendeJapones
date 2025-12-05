<div align="center">

# 🏯 Kotodama: Aprende Japonés

### *El camino del idioma comienza con una sola palabra*

<img src="docs/screenshots/logo.png" alt="Kotodama Logo" width="200"/>

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen?style=for-the-badge&logo=github)](https://github.com/Guarghuk/AprendeJapones)
[![Version](https://img.shields.io/badge/version-1.0.0-blue?style=for-the-badge)](https://github.com/Guarghuk/AprendeJapones/releases)
[![License](https://img.shields.io/badge/license-MIT-green?style=for-the-badge)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-API%2024+-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange?style=for-the-badge)](https://developer.android.com/topic/architecture)

</div>

---

## 📱 Descripción del Proyecto

**Kotodama: Aprende Japonés** es una aplicación móvil educativa desarrollada en Android nativo con Kotlin, diseñada específicamente para hispanohablantes que desean iniciar o fortalecer su aprendizaje del idioma japonés. El nombre "Kotodama" (言霊) proviene del concepto japonés que significa "el espíritu de las palabras", reflejando nuestra filosofía de que cada palabra aprendida cobra vida y significado en el usuario.

El mercado actual de aplicaciones para aprender japonés presenta una problemática significativa: la mayoría de las herramientas disponibles están diseñadas para angloparlantes, carecen de explicaciones culturales contextualizadas, o implementan metodologías poco efectivas que resultan en altas tasas de abandono. **Kotodama** surge como respuesta a esta necesidad, ofreciendo una experiencia de aprendizaje inmersiva, gamificada y culturalmente relevante que conecta directamente con el usuario hispanohablante.

Nuestra aplicación está dirigida a estudiantes universitarios, profesionales interesados en la cultura japonesa, entusiastas del anime y manga, y cualquier persona que busque una herramienta efectiva y accesible para dominar los fundamentos del japonés. A través de lecciones estructuradas, ejercicios interactivos y un sistema de progresión motivador, **Kotodama** transforma el desafiante proceso de aprender japonés en una experiencia gratificante y sostenible.

---

## ✨ Características Principales

| Característica | Descripción |
|:--------------|:------------|
| 🎌 **Sistema de Escritura Completo** | Aprende Hiragana, Katakana y los Kanji más utilizados con trazos animados, pronunciación nativa y ejercicios de reconocimiento. |
| 🎮 **Gamificación Inteligente** | Sistema de puntos, rachas diarias, logros desbloqueables y niveles de progresión que mantienen la motivación del usuario. |
| 🏯 **Comunidad Integrada** | Comunidad virtual en la cual los usuarios pueden ayudarse a entender lecciones, preguntar sobre cultura y expresión dentro de la aplicación. |
| 📚 **Lecciones Contextualizadas** | Contenido organizado por situaciones reales: presentaciones, restaurantes, transporte, compras y conversaciones cotidianas. |
| 📊 **Seguimiento de Progreso** | Dashboard personalizado con estadísticas de aprendizaje, áreas de mejora identificadas y recomendaciones adaptativas. |
| 💾 **Funcionalidad Offline** | Lecciones para estudiar sin conexión a internet, ideal para transporte público o viajes. |

---

## 📸 Galería de Interfaz

<div align="center">

| 🏠 Inicio / Login | 📖 Funcionalidad Principal | 📋 Detalles |
|:-----------------:|:--------------------------:|:-----------:|
| ![Pantalla Principal](docs/screenshots/screenshot1.jpeg) | ![Pantalla Lecciones](docs/screenshots/screenshot2.jpeg) | ![Pantalla Quiz](docs/screenshots/screenshot3.jpeg) |
| *Pantalla de bienvenida* | *Lección de Hiragana* | *Sistema de Tienda* |
| ![Pantalla Progreso](docs/screenshots/screenshot4.jpeg) | ![Pantalla Kanji](docs/screenshots/screenshot5.jpeg) | ![Pantalla Logros](docs/screenshots/screenshot6.jpeg) |
| *Dashboard de progreso* | *Aprendizaje de Kanji* | *Sistema de Comunidad* |

</div>

---

## 🛠 Stack Tecnológico y Arquitectura

### Tecnologías Utilizadas

| Categoría | Tecnología | Propósito |
|:----------|:-----------|:----------|
| **Lenguaje** | ![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white) | Lenguaje principal de desarrollo |
| **IDE** | ![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?style=flat-square&logo=android-studio&logoColor=white) | Entorno de desarrollo integrado |
| **UI Framework** | ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat-square&logo=jetpack-compose&logoColor=white) | Framework declarativo de UI |
| **Base de Datos** | ![Room](https://img.shields.io/badge/Room-FF6F00?style=flat-square&logo=sqlite&logoColor=white) | Persistencia local de datos |
| **Async** | ![Coroutines](https://img.shields.io/badge/Coroutines-7F52FF?style=flat-square&logo=kotlin&logoColor=white) | Programación asíncrona |
| **Diseño** | ![Material 3](https://img.shields.io/badge/Material%20Design%203-757575?style=flat-square&logo=material-design&logoColor=white) | Sistema de diseño UI/UX |
| **Control de Versiones** | ![Git](https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white) | Gestión del código fuente |

### Arquitectura MVVM

```
┌─────────────────────────────────────────────────────────────┐
│                        📱 VIEW                              │
│              (Activities / Composables)                     │
│         Observa estados y emite eventos de usuario          │
└─────────────────────┬───────────────────────────────────────┘
                      │ ⬆️ StateFlow / LiveData
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                    🧠 VIEWMODEL                             │
│              (Lógica de presentación)                       │
│      Procesa eventos, gestiona estado UI, coordina          │
└─────────────────────┬───────────────────────────────────────┘
                      │ ⬆️ Suspend Functions / Flow
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                    📦 REPOSITORY                           │
│              (Abstracción de datos)                        │
│         Única fuente de verdad para el ViewModel           │
└──────────┬─────────────────────────────────────────────────┘
           │                                 
           ▼                                 
┌─────────────────────────────────────────────────────────────┐
│                    🗄️ LOCAL (Room Database)                │
│                    Base de datos SQLite                     │
└─────────────────────────────────────────────────────────────┘
```

La arquitectura **MVVM (Model-View-ViewModel)** fue seleccionada por las siguientes razones técnicas:

- **Separación de responsabilidades**: Cada capa tiene una función específica, facilitando el mantenimiento y las pruebas unitarias.
- **Reactividad**: El uso de `StateFlow` y `LiveData` permite que la UI reaccione automáticamente a cambios en los datos.
- **Lifecycle-aware**: Los ViewModels sobreviven a cambios de configuración, evitando pérdida de datos y llamadas redundantes.
- **Testabilidad**: La lógica de negocio aislada en ViewModels y Repositories permite pruebas unitarias sin dependencias de Android.

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

## 📝 Documentación Técnica

### Ejemplo 1: ViewModel para Gestión de Lecciones

```kotlin
/**
 * ViewModel responsable de gestionar el estado y la lógica de negocio
 * relacionada con las lecciones de aprendizaje de japonés.
 *
 * Este ViewModel coordina la obtención de lecciones desde el repositorio,
 * gestiona el estado de carga y maneja los eventos de interacción del usuario
 * con el contenido educativo.
 *
 * @property lessonRepository Repositorio que abstrae el acceso a datos de lecciones.
 * @property userProgressRepository Repositorio para gestionar el progreso del usuario.
 *
 * @author Equipo Kotodama
 * @since 1.0.0
 */
@HiltViewModel
class LessonViewModel @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val userProgressRepository: UserProgressRepository
) : ViewModel() {

    /**
     * Estado interno mutable de la UI.
     * Solo modificable desde dentro del ViewModel.
     */
    private val _uiState = MutableStateFlow(LessonUiState())
    
    /**
     * Estado público inmutable expuesto a la capa de presentación.
     * La UI observa este StateFlow para actualizarse reactivamente.
     */
    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    /**
     * Canal para eventos únicos que no deben reemitirse en recomposiciones.
     * Utilizado para navegación, snackbars y efectos secundarios.
     */
    private val _events = Channel<LessonEvent>(Channel.BUFFERED)
    val events: Flow<LessonEvent> = _events.receiveAsFlow()

    init {
        loadLessons()
    }

    /**
     * Carga las lecciones disponibles desde el repositorio.
     *
     * Esta función obtiene las lecciones de manera asíncrona, actualiza el estado
     * de carga de la UI y maneja posibles errores de red o base de datos.
     *
     * El flujo de ejecución:
     * 1. Establece el estado de carga a `true`
     * 2. Solicita las lecciones al repositorio
     * 3. Actualiza el estado con las lecciones obtenidas
     * 4. En caso de error, emite un evento de error y actualiza el estado
     *
     * @see LessonRepository.getAllLessons
     */
    fun loadLessons() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            lessonRepository.getAllLessons()
                .catch { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            error = exception.localizedMessage ?: "Error desconocido"
                        ) 
                    }
                    _events.send(LessonEvent.ShowError("No se pudieron cargar las lecciones"))
                }
                .collect { lessons ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            lessons = lessons,
                            error = null
                        ) 
                    }
                }
        }
    }

    /**
     * Marca una lección como completada y actualiza el progreso del usuario.
     *
     * @param lessonId Identificador único de la lección completada.
     * @param score Puntuación obtenida por el usuario (0-100).
     * @param timeSpentSeconds Tiempo empleado en completar la lección en segundos.
     *
     * @throws IllegalArgumentException Si el score está fuera del rango válido.
     */
    fun completeLesson(lessonId: String, score: Int, timeSpentSeconds: Long) {
        require(score in 0..100) { "El score debe estar entre 0 y 100" }
        
        viewModelScope.launch {
            val progress = UserProgress(
                lessonId = lessonId,
                score = score,
                completedAt = System.currentTimeMillis(),
                timeSpentSeconds = timeSpentSeconds
            )
            
            userProgressRepository.saveProgress(progress)
                .onSuccess {
                    _events.send(LessonEvent.LessonCompleted(score))
                    loadLessons() // Recargar para actualizar estados
                }
                .onFailure { error ->
                    _events.send(LessonEvent.ShowError("Error al guardar progreso"))
                }
        }
    }
}

/**
 * Data class que representa el estado de la UI de lecciones.
 *
 * @property isLoading Indica si se están cargando datos.
 * @property lessons Lista de lecciones disponibles.
 * @property error Mensaje de error si ocurrió algún problema.
 */
data class LessonUiState(
    val isLoading: Boolean = false,
    val lessons: List<Lesson> = emptyList(),
    val error: String? = null
)

/**
 * Sealed class que define los eventos únicos emitidos por el ViewModel.
 */
sealed class LessonEvent {
    data class ShowError(val message: String) : LessonEvent()
    data class LessonCompleted(val score: Int) : LessonEvent()
}
```

### Ejemplo 2: Adapter con RecyclerView para Lista de Caracteres

```kotlin
/**
 * Adapter de RecyclerView para mostrar una lista de caracteres japoneses
 * (Hiragana, Katakana o Kanji) con soporte para interacciones del usuario.
 *
 * Implementa [ListAdapter] con [DiffUtil] para actualizaciones eficientes
 * de la lista, calculando automáticamente las diferencias entre datasets
 * y animando los cambios de manera fluida.
 *
 * Características implementadas:
 * - ViewBinding para acceso seguro a vistas
 * - DiffUtil para rendimiento optimizado
 * - Click listeners para reproducción de audio y selección
 * - Soporte para estados visuales (aprendido, en progreso, nuevo)
 *
 * @property onCharacterClick Callback invocado cuando el usuario toca un carácter.
 * @property onAudioClick Callback invocado cuando el usuario solicita reproducir audio.
 *
 * @author Equipo Kotodama
 * @since 1.0.0
 */
class JapaneseCharacterAdapter(
    private val onCharacterClick: (JapaneseCharacter) -> Unit,
    private val onAudioClick: (JapaneseCharacter) -> Unit
) : ListAdapter<JapaneseCharacter, JapaneseCharacterAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    /**
     * Crea una nueva instancia de ViewHolder inflando el layout del item.
     *
     * @param parent ViewGroup padre donde se añadirá la vista.
     * @param viewType Tipo de vista (no utilizado en esta implementación).
     * @return Nueva instancia de [CharacterViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemJapaneseCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding)
    }

    /**
     * Vincula los datos de un carácter japonés con el ViewHolder correspondiente.
     *
     * @param holder ViewHolder que mostrará los datos.
     * @param position Posición del item en la lista.
     */
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder que mantiene las referencias a las vistas de cada item
     * y gestiona la vinculación de datos.
     *
     * @property binding Binding generado para el layout del item.
     */
    inner class CharacterViewHolder(
        private val binding: ItemJapaneseCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Vincula un objeto [JapaneseCharacter] con las vistas del item.
         *
         * Configura:
         * - El carácter japonés principal
         * - La romanización (romaji)
         * - El indicador de estado de aprendizaje
         * - Los listeners de click para interacción
         *
         * @param character Objeto con los datos del carácter a mostrar.
         */
        fun bind(character: JapaneseCharacter) {
            with(binding) {
                // Configurar textos principales
                tvCharacter.text = character.character
                tvRomaji.text = character.romaji
                tvMeaning.text = character.meaning
                
                // Configurar indicador de estado con colores semánticos
                val statusColor = when (character.learningStatus) {
                    LearningStatus.MASTERED -> R.color.status_mastered
                    LearningStatus.IN_PROGRESS -> R.color.status_in_progress
                    LearningStatus.NEW -> R.color.status_new
                }
                statusIndicator.setBackgroundColor(
                    ContextCompat.getColor(root.context, statusColor)
                )
                
                // Configurar accesibilidad
                root.contentDescription = buildString {
                    append("Carácter ${character.character}, ")
                    append("se pronuncia ${character.romaji}, ")
                    append("significa ${character.meaning}")
                }

                // Configurar listeners de interacción
                root.setOnClickListener { 
                    onCharacterClick(character) 
                }
                
                btnPlayAudio.setOnClickListener { 
                    onAudioClick(character) 
                }
                
                // Animación sutil al hacer bind
                root.alpha = 0f
                root.animate()
                    .alpha(1f)
                    .setDuration(150)
                    .start()
            }
        }
    }

    /**
     * Callback de DiffUtil para calcular diferencias entre listas de caracteres.
     *
     * Optimiza el rendimiento del RecyclerView al determinar qué items
     * han cambiado, se han añadido o eliminado, evitando recargas completas.
     */
    private class CharacterDiffCallback : DiffUtil.ItemCallback<JapaneseCharacter>() {
        
        /**
         * Determina si dos items representan el mismo objeto.
         *
         * @param oldItem Item de la lista anterior.
         * @param newItem Item de la lista nueva.
         * @return `true` si ambos items tienen el mismo identificador.
         */
        override fun areItemsTheSame(
            oldItem: JapaneseCharacter, 
            newItem: JapaneseCharacter
        ): Boolean = oldItem.id == newItem.id

        /**
         * Determina si el contenido de dos items es idéntico.
         *
         * @param oldItem Item de la lista anterior.
         * @param newItem Item de la lista nueva.
         * @return `true` si todos los campos de ambos items son iguales.
         */
        override fun areContentsTheSame(
            oldItem: JapaneseCharacter, 
            newItem: JapaneseCharacter
        ): Boolean = oldItem == newItem
    }
}

/**
 * Data class que representa un carácter japonés en el sistema.
 *
 * @property id Identificador único del carácter.
 * @property character El carácter japonés (ej: "あ", "ア", "愛").
 * @property romaji Romanización del carácter (ej: "a", "ai").
 * @property meaning Significado o contexto del carácter.
 * @property audioUrl URL del archivo de audio con la pronunciación.
 * @property learningStatus Estado actual de aprendizaje del usuario.
 */
data class JapaneseCharacter(
    val id: String,
    val character: String,
    val romaji: String,
    val meaning: String,
    val audioUrl: String,
    val learningStatus: LearningStatus
)

/**
 * Enum que representa los posibles estados de aprendizaje de un carácter.
 */
enum class LearningStatus {
    /** El usuario ha dominado completamente el carácter */
    MASTERED,
    /** El usuario está en proceso de aprender el carácter */
    IN_PROGRESS,
    /** El usuario aún no ha estudiado el carácter */
    NEW
}
```

---

## 📊 Validación y Métricas

### Pruebas con Usuarios

Se realizó una fase de validación con usuarios reales para evaluar la usabilidad, efectividad pedagógica y satisfacción general con la aplicación.

| Métrica | Resultado |
|:--------|:----------|
| **Participantes** | 10 usuarios |
| **Perfil demográfico** | Estudiantes universitarios (18-25 años) |
| **Duración de prueba** | 1 dia por usuario |
| **Calificación promedio** | ⭐ **4.5 / 5.0** |

### Resultados Detallados

```
📈 Satisfacción General
██████████████████████████████████████████████░░░░  90%

📱 Facilidad de Uso
████████████████████████████████████████████████░░  96%

🎯 Efectividad de Aprendizaje
██████████████████████████████████████████░░░░░░░░  84%

🎨 Diseño Visual
███████████████████████████████████████████████░░░  94%

🔄 Probabilidad de Recomendación
████████████████████████████████████████████░░░░░░  88%
```

### Feedback Cualitativo Destacado

> *"La interfaz es muy intuitiva y los ejercicios de trazado me ayudaron mucho a memorizar el Hiragana."* — Usuario #3

> *"Me encanta el sistema de rachas, me motiva a practicar todos los días."* — Usuario #7

> *"Sería genial poder competir con amigos. ¡Añadan un modo multijugador!"* — Usuario #5 (Sugerencia para v2.0)

---

## ⚙️ Instrucciones de Instalación

### Requisitos Previos

- **Android Studio** Hedgehog (2023.1.1) o superior
- **JDK** 17 o superior
- **Android SDK** 34
- **Dispositivo/Emulador** con Android 7.0 (API 24) o superior

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Guarghuk/AprendeJapones.git
   ```

2. **Navegar al directorio del proyecto**
   ```bash
   cd AprendeJapones
   ```

3. **Abrir en Android Studio**
   - Iniciar Android Studio
   - Seleccionar `File > Open`
   - Navegar hasta la carpeta del proyecto y seleccionarla
   - Esperar a que Gradle sincronice las dependencias

4. **Configurar el emulador o dispositivo**
   - **Emulador**: `Tools > Device Manager > Create Device`
   - **Dispositivo físico**: Habilitar "Depuración USB" en opciones de desarrollador

5. **Compilar y ejecutar**
   - Click en el botón ▶️ `Run 'app'`
   - O usar el atajo: `Shift + F10` (Windows/Linux) / `Control + R` (macOS)

6. **¡Listo!** 🎉
   - La aplicación se instalará y ejecutará automáticamente

### Solución de Problemas Comunes

| Problema | Solución |
|:---------|:---------|
| Error de sincronización Gradle | `File > Invalidate Caches / Restart` |
| SDK no encontrado | Verificar `local.properties` contiene `sdk.dir` correcto |
| Emulador lento | Habilitar aceleración por hardware (HAXM/Hyper-V) |

---

## 👥 Autores

<div align="center">

| 👤 Nombre | 🎭 Rol | 🔗 GitHub |
|:----------|:-------|:----------|
| José Enrique Ramírez Guerrero | Desarrollador Principal / Project Lead | [![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github)](https://github.com/Guarghuk) |
| José Emmanuel Rodríguez Arvizu | Desarrollador UI/UX / QA Tester | [![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github)](https://github.com/ALTMAXXUS777) |

</div>

---

<div align="center">

### 🌸 ありがとうございます (¡Gracias!)

*Desarrollado con ❤️ para la comunidad hispanohablante*

**Universidad Tecnologica del Norte de Guanajuato — Desarrollo de Aplicaciones Móviles**

*Cuatrimeste 4 - 2025*

---

[![Made with Kotlin](https://img.shields.io/badge/Made%20with-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Built for Android](https://img.shields.io/badge/Built%20for-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)

</div>
