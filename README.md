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

### 🛠️ Guía de Creación de la Aplicación

Esta guía explica paso a paso cómo fue construida la aplicación Kotodama, describiendo cada archivo, su ubicación y propósito. Sigue este orden para recrear la estructura del proyecto.

---

### 📍 Paso 1: Configuración Inicial del Proyecto

**Ubicación:** `app/src/main/java/com/example/aprendejapones/`

#### 1.1 Clase Application
**Archivo:** `KotodamaApplication.kt`
```
Propósito: Punto de entrada de la aplicación. Inicializa Hilt para inyección de 
dependencias y configura WorkManager para tareas en segundo plano (verificación 
de racha diaria).
```

#### 1.2 Activity Principal
**Archivo:** `MainActivity.kt`
```
Propósito: Única Activity de la app (Single Activity Pattern). Configura el tema 
de Compose, solicita permisos de notificaciones y renderiza el NavGraph según 
el estado de autenticación del usuario.
```

---

### 📍 Paso 2: Capa de Dominio (Lógica de Negocio)

**Ubicación:** `domain/`

#### 2.1 Modelos de Datos
**Carpeta:** `domain/model/`

- **`User.kt`** - Define el modelo de usuario con propiedades: id, username, rank, level, XP, streak, drops. Incluye también `DailyChallenge`, `LessonFunction` y `KitsuneMessage`.

- **`FirebaseModels.kt`** - Modelos para Firestore: `UsuariosFirestore`, `Publicaciones`, `Comentarios`, `Likes`. Contiene también modelos legacy para compatibilidad.

- **`Response.kt`** - Sealed class que envuelve resultados de operaciones async con estados: `Loading`, `Success`, `Failure`.

#### 2.2 Interfaces de Repositorios
**Carpeta:** `domain/repository/`

Define los contratos (interfaces) que la capa de datos debe implementar:
- `UserRepository.kt` - Gestión del usuario local
- `AuthRepository.kt` - Autenticación (login, registro, logout)
- `LessonRepository.kt` - Gestión de lecciones
- `ProgressRepository.kt` - Seguimiento de progreso
- `AchievementRepository.kt` - Sistema de logros
- `CommunityRepository.kt` - Funcionalidades de comunidad
- `PostRepository.kt` - Publicaciones
- `FirestoreUserRepository.kt` - Sincronización con la nube

#### 2.3 Casos de Uso
**Carpeta:** `domain/usecases/`

- **`GetPostsUseCase.kt`** - Obtiene el feed de publicaciones de la comunidad
- **`GetPostByUserId.kt`** - Filtra publicaciones por usuario

#### 2.4 Managers
**Carpeta:** `domain/manager/`

- **`StreakManager.kt`** - Lógica para calcular y actualizar la racha de estudio diaria

---

### 📍 Paso 3: Capa de Datos (Persistencia)

**Ubicación:** `data/`

#### 3.1 Base de Datos Local (Room)
**Carpeta:** `data/local/database/`

- **`AppDatabase.kt`** - Configuración de Room Database, define versión y entidades

- **Entidades** (`entity/`):
  - `UsuariosLocalEntity.kt` - Tabla de usuarios
  - `ProgresoCategoriaEntity.kt` - Progreso por categoría
  - `HistorialLeccionesEntity.kt` - Historial de lecciones
  - `LogrosLocalEntity.kt` - Logros desbloqueados

- **DAOs** (`dao/`): Interfaces con queries SQL para cada entidad
  - `UsuariosLocalDao.kt`, `ProgresoCategoriaDao.kt`, etc.

#### 3.2 Preferencias
**Carpeta:** `data/local/preferences/`

- **`PreferencesManager.kt`** - Usa DataStore para guardar preferencias: si completó onboarding, horarios de recordatorios, etc.

#### 3.3 Firebase
**Carpeta:** `data/firebase/`

- **`FirebaseManager.kt`** - Singleton con referencias a colecciones de Firestore (users, posts, comments, likes)

#### 3.4 Implementaciones de Repositorios
**Carpeta:** `data/repository/`

Implementan las interfaces definidas en `domain/repository/`:
- `UserRepositoryImpl.kt` - Gestiona usuario local, XP, nivel, drops
- `AuthRepositoryImpl.kt` - Conecta con Firebase Auth
- `LessonRepositoryImpl.kt` - Carga contenido de lecciones
- `ProgressRepositoryImpl.kt` - Actualiza progreso en Room
- `FirestoreCommunityRepositoryImpl.kt` - CRUD de publicaciones en Firestore

#### 3.5 Mappers
**Carpeta:** `data/mapper/`

- **`UserMapper.kt`** - Convierte entre entidades de Room y modelos de dominio

---

### 📍 Paso 4: Inyección de Dependencias (Hilt)

**Ubicación:** `di/`

- **`AppModule.kt`** - Dependencias generales (Context, PreferencesManager)
- **`DatabaseModule.kt`** - Provee AppDatabase y todos los DAOs
- **`FirebaseModule.kt`** - Provee FirebaseAuth, FirebaseFirestore
- **`RepositoryModule.kt`** - Vincula interfaces con implementaciones usando `@Binds`

---

### 📍 Paso 5: Capa de Presentación (UI)

**Ubicación:** `presentation/`

#### 5.1 Tema de la Aplicación
**Carpeta:** `presentation/theme/`

- **`Color.kt`** - Paleta de colores (PrimaryGreen, SecondaryBlue, etc.)
- **`Type.kt`** - Tipografías personalizadas
- **`Theme.kt`** - Configuración de Material 3 Theme

#### 5.2 Navegación
**Carpeta:** `presentation/navigation/`

- **`Screen.kt`** - Sealed class con todas las rutas: Home, Profile, Community, Lesson, etc.
- **`NavGraph.kt`** - Configuración del Navigation Compose con todos los destinos
- **`BottomNavigationBar.kt`** - Barra inferior con 4 tabs principales

#### 5.3 Componentes Reutilizables
**Carpeta:** `presentation/components/`

- **`cards/`** - Tarjetas: ProgressCard, DailyChallengeCard, PostCard, FunctionCard, etc.
- **`common/`** - Elementos comunes: StatBadge

#### 5.4 Pantallas (Features)
**Carpeta:** `presentation/screens/`

Cada feature tiene su propia carpeta con patrón MVI:

**Estructura por pantalla:**
```
[feature]/
├── [Feature]Screen.kt     → UI Composable
├── [Feature]State.kt      → Estado de la UI (data class)
├── [Feature]ViewModel.kt  → Lógica de presentación
├── [Feature]Event.kt      → Eventos del usuario (opcional)
└── [Feature]Effect.kt     → Efectos one-shot (opcional)
```

**Pantallas implementadas:**
- `auth/` - Login y Registro
- `onboarding/` - Introducción con páginas (Welcome, Features, Ready)
- `home/` - Dashboard principal con lecciones
- `lesson/` - Ejercicios y contenido educativo
- `profile/` - Perfil propio, edición, perfil de otros usuarios
- `community/` - Feed de publicaciones
- `newpost/` - Crear publicación
- `achievements/` - Sistema de logros
- `stats/` - Estadísticas detalladas
- `shop/` - Tienda con monedas virtuales
- `menu/` - Configuración y opciones
- `dailygoal/` - Configurar meta diaria
- `reminders/` - Configurar recordatorios
- `sync/` - Sincronización con la nube
- `splash/` - Lógica inicial de la app

---

### 📍 Paso 6: Sistema de Notificaciones

**Ubicación:** `notification/`

- **`NotificationHelper.kt`** - Crea y muestra notificaciones para recordatorios de estudio y alertas de racha

---

### 📍 Paso 7: Tareas en Segundo Plano

**Ubicación:** `workers/`

- **`StreakWorker.kt`** - Se ejecuta a medianoche para verificar si el usuario perdió su racha
- **`ReminderWorker.kt`** - Envía recordatorios de estudio según horarios configurados
- **`WorkManagerScheduler.kt`** - Programa los workers con WorkManager

---

### 📍 Paso 8: Utilidades

**Ubicación:** `utils/`

- **`TimeUtils.kt`** - Funciones para formatear fechas y calcular tiempo restante
- **`MockData.kt`** - Datos de prueba para desarrollo

---

### 🔄 Flujo de Datos (MVI Pattern)

```
┌─────────────┐     ┌──────────────┐     ┌──────────────┐     ┌─────────────┐
│   Usuario   │ ──► │    Screen    │ ──► │  ViewModel   │ ──► │ Repository  │
│  (Interacción)    │  (Composable)│     │   (Lógica)   │     │   (Datos)   │
└─────────────┘     └──────────────┘     └──────────────┘     └─────────────┘
                           ▲                    │
                           │                    ▼
                    ┌──────┴────────────────────────┐
                    │     State / Effect / Event    │
                    └───────────────────────────────┘
```

1. El usuario interactúa con la **Screen**
2. La Screen envía un **Event** al **ViewModel**
3. El ViewModel procesa el evento y llama al **Repository**
4. El Repository obtiene/guarda datos en Room o Firestore
5. El ViewModel actualiza el **State**
6. La Screen observa el State y se re-renderiza

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
