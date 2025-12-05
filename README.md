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

### 🌳 Estructura de Directorios del Código Fuente

```
app/src/main/java/com/example/aprendejapones/
├── KotodamaApplication.kt              # Clase Application principal
├── MainActivity.kt                      # Activity principal de la app
│
├── data/                                # 📦 Capa de Datos
│   ├── firebase/
│   │   └── FirebaseManager.kt
│   ├── local/
│   │   ├── database/
│   │   │   ├── AppDatabase.kt
│   │   │   ├── dao/
│   │   │   │   ├── HistorialLeccionesDao.kt
│   │   │   │   ├── LogrosLocalDao.kt
│   │   │   │   ├── ProgresoCategoriaDao.kt
│   │   │   │   └── UsuariosLocalDao.kt
│   │   │   └── entity/
│   │   │       ├── HistorialLeccionesEntity.kt
│   │   │       ├── LogrosLocalEntity.kt
│   │   │       ├── ProgresoCategoriaEntity.kt
│   │   │       └── UsuariosLocalEntity.kt
│   │   └── preferences/
│   │       └── PreferencesManager.kt
│   ├── mapper/
│   │   └── UserMapper.kt
│   └── repository/
│       ├── AchievementRepositoryImpl.kt
│       ├── AuthRepositoryImpl.kt
│       ├── FirestoreCommunityRepositoryImpl.kt
│       ├── FirestoreUserRepositoryImpl.kt
│       ├── LessonRepositoryImpl.kt
│       ├── PostRepositoryImpl.kt
│       ├── ProgressRepositoryImpl.kt
│       └── UserRepositoryImpl.kt
│
├── di/                                  # 💉 Inyección de Dependencias (Hilt)
│   ├── AppModule.kt
│   ├── DatabaseModule.kt
│   ├── FirebaseModule.kt
│   └── RepositoryModule.kt
│
├── domain/                              # 🧠 Capa de Dominio
│   ├── manager/
│   │   └── StreakManager.kt
│   ├── model/
│   │   ├── FirebaseModels.kt
│   │   ├── Response.kt
│   │   └── User.kt
│   ├── repository/
│   │   ├── AchievementRepository.kt
│   │   ├── AuthRepository.kt
│   │   ├── CommunityRepository.kt
│   │   ├── FirestoreUserRepository.kt
│   │   ├── LessonContentRepository.kt
│   │   ├── LessonRepository.kt
│   │   ├── PostRepository.kt
│   │   ├── ProgressRepository.kt
│   │   └── UserRepository.kt
│   └── usecases/
│       ├── GetPostByUserId.kt
│       └── GetPostsUseCase.kt
│
├── notification/                        # 🔔 Sistema de Notificaciones
│   └── NotificationHelper.kt
│
├── presentation/                        # 🎨 Capa de Presentación (UI)
│   ├── components/
│   │   ├── cards/
│   │   │   ├── AchievementsPreviewCard.kt
│   │   │   ├── DailyChallengeCard.kt
│   │   │   ├── FunctionCard.kt
│   │   │   ├── KitsuneMessageCard.kt
│   │   │   ├── PostCard.kt
│   │   │   ├── ProgressCard.kt
│   │   │   ├── RecentActivityCard.kt
│   │   │   ├── StatsCard.kt
│   │   │   └── UserInfoCard.kt
│   │   └── common/
│   │       └── StatBadge.kt
│   ├── navigation/
│   │   ├── BottomNavigationBar.kt
│   │   ├── NavGraph.kt
│   │   └── Screen.kt
│   ├── screens/
│   │   ├── achievements/
│   │   │   ├── AchievementsScreen.kt
│   │   │   ├── AchievementsState.kt
│   │   │   └── AchievementsViewModel.kt
│   │   ├── auth/
│   │   │   ├── AuthEffect.kt
│   │   │   ├── AuthEvent.kt
│   │   │   ├── AuthState.kt
│   │   │   ├── AuthViewModel.kt
│   │   │   ├── LoginScreen.kt
│   │   │   └── RegisterScreen.kt
│   │   ├── community/
│   │   │   ├── CommunityScreen.kt
│   │   │   ├── CommunityState.kt
│   │   │   └── CommunityViewModel.kt
│   │   ├── dailygoal/
│   │   │   ├── DailyGoalEffect.kt
│   │   │   ├── DailyGoalEvent.kt
│   │   │   ├── DailyGoalScreen.kt
│   │   │   ├── DailyGoalState.kt
│   │   │   └── DailyGoalViewModel.kt
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   │   ├── HomeState.kt
│   │   │   └── HomeViewModel.kt
│   │   ├── lesson/
│   │   │   ├── LessonScreen.kt
│   │   │   ├── LessonState.kt
│   │   │   └── LessonViewModel.kt
│   │   ├── menu/
│   │   │   ├── MenuScreen.kt
│   │   │   ├── MenuState.kt
│   │   │   └── MenuViewModel.kt
│   │   ├── newpost/
│   │   │   ├── NewPostEffect.kt
│   │   │   ├── NewPostEvent.kt
│   │   │   ├── NewPostScreen.kt
│   │   │   ├── NewPostState.kt
│   │   │   └── NewPostViewModel.kt
│   │   ├── onboarding/
│   │   │   ├── OnBoardingScreen.kt
│   │   │   ├── OnBoardingState.kt
│   │   │   ├── OnBoardingViewModel.kt
│   │   │   └── pages/
│   │   │       ├── FeaturesPage.kt
│   │   │       ├── ReadyPage.kt
│   │   │       └── WelcomePage.kt
│   │   ├── profile/
│   │   │   ├── EditProfileScreen.kt
│   │   │   ├── EditProfileState.kt
│   │   │   ├── EditProfileViewModel.kt
│   │   │   ├── ProfileScreen.kt
│   │   │   ├── ProfileState.kt
│   │   │   ├── ProfileViewModel.kt
│   │   │   ├── UserProfileScreen.kt
│   │   │   ├── UserProfileState.kt
│   │   │   └── UserProfileViewModel.kt
│   │   ├── reminders/
│   │   │   ├── RemindersEffect.kt
│   │   │   ├── RemindersEvent.kt
│   │   │   ├── RemindersScreen.kt
│   │   │   ├── RemindersState.kt
│   │   │   └── RemindersViewModel.kt
│   │   ├── shop/
│   │   │   ├── ShopScreen.kt
│   │   │   ├── ShopState.kt
│   │   │   └── ShopViewModel.kt
│   │   ├── splash/
│   │   │   └── SplashViewModel.kt
│   │   ├── stats/
│   │   │   ├── StatsScreen.kt
│   │   │   ├── StatsState.kt
│   │   │   └── StatsViewModel.kt
│   │   └── sync/
│   │       ├── SyncEffect.kt
│   │       ├── SyncEvent.kt
│   │       ├── SyncScreen.kt
│   │       ├── SyncState.kt
│   │       └── SyncViewModel.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
├── utils/                               # 🔧 Utilidades
│   ├── MockData.kt
│   └── TimeUtils.kt
│
└── workers/                             # ⏰ Background Workers
    ├── ReminderWorker.kt
    ├── StreakWorker.kt
    └── WorkManagerScheduler.kt
```

---

### 📚 Diccionario de Clases

#### 📦 Paquete: `data.firebase`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `FirebaseManager.kt` | Singleton/Manager | Gestiona las referencias a las colecciones de Firestore y proporciona acceso centralizado a Firebase |

#### 📦 Paquete: `data.local.database`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `AppDatabase.kt` | Database | Configuración de Room Database con DAOs y entidades |
| `HistorialLeccionesDao.kt` | DAO Interface | Operaciones CRUD para el historial de lecciones completadas |
| `LogrosLocalDao.kt` | DAO Interface | Operaciones CRUD para logros almacenados localmente |
| `ProgresoCategoriaDao.kt` | DAO Interface | Operaciones CRUD para el progreso por categoría de lección |
| `UsuariosLocalDao.kt` | DAO Interface | Operaciones CRUD para datos del usuario local |
| `HistorialLeccionesEntity.kt` | Entity | Entidad Room para historial de lecciones |
| `LogrosLocalEntity.kt` | Entity | Entidad Room para logros del usuario |
| `ProgresoCategoriaEntity.kt` | Entity | Entidad Room para progreso por categoría |
| `UsuariosLocalEntity.kt` | Entity | Entidad Room para información del usuario |

#### 📦 Paquete: `data.local.preferences`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `PreferencesManager.kt` | Manager | Gestiona preferencias del usuario con DataStore (onboarding, recordatorios) |

#### 📦 Paquete: `data.mapper`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `UserMapper.kt` | Mapper | Convierte entre entidades de Room y modelos de dominio |

#### 📦 Paquete: `data.repository`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `AchievementRepositoryImpl.kt` | Repository Impl | Implementación del repositorio de logros |
| `AuthRepositoryImpl.kt` | Repository Impl | Implementación de autenticación con Firebase Auth |
| `FirestoreCommunityRepositoryImpl.kt` | Repository Impl | Gestión de publicaciones, likes y comentarios en Firestore |
| `FirestoreUserRepositoryImpl.kt` | Repository Impl | Sincronización de usuario entre local y Firestore |
| `LessonRepositoryImpl.kt` | Repository Impl | Gestión de lecciones y contenido educativo |
| `PostRepositoryImpl.kt` | Repository Impl | Operaciones CRUD para publicaciones de la comunidad |
| `ProgressRepositoryImpl.kt` | Repository Impl | Seguimiento del progreso del usuario |
| `UserRepositoryImpl.kt` | Repository Impl | Gestión del usuario local (XP, nivel, racha, drops) |

#### 📦 Paquete: `di` (Dependency Injection)

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `AppModule.kt` | Hilt Module | Proporciona dependencias generales de la aplicación |
| `DatabaseModule.kt` | Hilt Module | Proporciona instancias de Room Database y DAOs |
| `FirebaseModule.kt` | Hilt Module | Proporciona instancias de Firebase (Auth, Firestore) |
| `RepositoryModule.kt` | Hilt Module | Vincula interfaces de repositorios con sus implementaciones |

#### 📦 Paquete: `domain.manager`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `StreakManager.kt` | Manager | Lógica de negocio para gestionar rachas de estudio diarias |

#### 📦 Paquete: `domain.model`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `FirebaseModels.kt` | Data Classes | Modelos para Firestore (Publicaciones, Comentarios, Likes, UsuariosFirestore) |
| `Response.kt` | Sealed Class | Wrapper para estados de operaciones async (Loading, Success, Failure) |
| `User.kt` | Data Classes | Modelos de dominio (User, DailyChallenge, LessonFunction, KitsuneMessage) |

#### 📦 Paquete: `domain.repository`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `AchievementRepository.kt` | Interface | Contrato para operaciones de logros |
| `AuthRepository.kt` | Interface | Contrato para autenticación (login, registro, logout) |
| `CommunityRepository.kt` | Interface | Contrato para funcionalidades de comunidad |
| `FirestoreUserRepository.kt` | Interface | Contrato para sincronización de usuario con Firestore |
| `LessonContentRepository.kt` | Interface | Contrato para contenido de lecciones |
| `LessonRepository.kt` | Interface | Contrato para gestión de lecciones |
| `PostRepository.kt` | Interface | Contrato para publicaciones |
| `ProgressRepository.kt` | Interface | Contrato para seguimiento de progreso |
| `UserRepository.kt` | Interface | Contrato para gestión del usuario local |

#### 📦 Paquete: `domain.usecases`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `GetPostByUserId.kt` | Use Case | Obtiene publicaciones filtradas por usuario |
| `GetPostsUseCase.kt` | Use Case | Obtiene el feed de publicaciones de la comunidad |

#### 📦 Paquete: `notification`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `NotificationHelper.kt` | Helper | Crea y muestra notificaciones del sistema (recordatorios, racha) |

#### 📦 Paquete: `presentation.components.cards`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `AchievementsPreviewCard.kt` | Composable | Tarjeta de vista previa de logros |
| `DailyChallengeCard.kt` | Composable | Tarjeta que muestra el desafío diario actual |
| `FunctionCard.kt` | Composable | Tarjeta para mostrar categorías de lecciones |
| `KitsuneMessageCard.kt` | Composable | Tarjeta con mensajes motivacionales del Kitsune |
| `PostCard.kt` | Composable | Tarjeta para publicaciones de la comunidad |
| `ProgressCard.kt` | Composable | Tarjeta de progreso del usuario |
| `RecentActivityCard.kt` | Composable | Tarjeta de actividad reciente |
| `StatsCard.kt` | Composable | Tarjeta de estadísticas |
| `UserInfoCard.kt` | Composable | Tarjeta con información del perfil de usuario |

#### 📦 Paquete: `presentation.components.common`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `StatBadge.kt` | Composable | Badge reutilizable para mostrar estadísticas |

#### 📦 Paquete: `presentation.navigation`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `BottomNavigationBar.kt` | Composable | Barra de navegación inferior con 4 destinos principales |
| `NavGraph.kt` | Navigation Graph | Configuración del grafo de navegación de Jetpack Compose |
| `Screen.kt` | Sealed Class | Define todas las rutas de navegación de la aplicación |

#### 📦 Paquete: `presentation.screens`

| Pantalla | Archivos | Responsabilidad |
|:---------|:---------|:----------------|
| **Achievements** | Screen, State, ViewModel | Visualización y desbloqueo de logros |
| **Auth** | Screen (Login/Register), State, ViewModel, Event, Effect | Autenticación de usuarios |
| **Community** | Screen, State, ViewModel | Feed de publicaciones y comunidad |
| **DailyGoal** | Screen, State, ViewModel, Event, Effect | Configuración de metas diarias |
| **Home** | Screen, State, ViewModel | Pantalla principal con lecciones y progreso |
| **Lesson** | Screen, State, ViewModel | Ejercicios y contenido de lecciones |
| **Menu** | Screen, State, ViewModel | Configuración y opciones de la app |
| **NewPost** | Screen, State, ViewModel, Event, Effect | Creación de nuevas publicaciones |
| **Onboarding** | Screen, State, ViewModel, Pages/ | Introducción para nuevos usuarios |
| **Profile** | Screen, State, ViewModel (x3: Profile, EditProfile, UserProfile) | Perfiles de usuario |
| **Reminders** | Screen, State, ViewModel, Event, Effect | Configuración de recordatorios |
| **Shop** | Screen, State, ViewModel | Tienda de items con monedas virtuales |
| **Splash** | ViewModel | Lógica de inicialización de la app |
| **Stats** | Screen, State, ViewModel | Estadísticas detalladas de aprendizaje |
| **Sync** | Screen, State, ViewModel, Event, Effect | Sincronización de datos con la nube |

#### 📦 Paquete: `presentation.theme`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `Color.kt` | Object | Definición de la paleta de colores de la app |
| `Theme.kt` | Composable | Configuración del tema Material 3 |
| `Type.kt` | Object | Definición de tipografías |

#### 📦 Paquete: `utils`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `MockData.kt` | Object | Datos de prueba para desarrollo y testing |
| `TimeUtils.kt` | Object | Funciones de utilidad para formateo de fechas y tiempo |

#### 📦 Paquete: `workers`

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `ReminderWorker.kt` | Worker | Ejecuta recordatorios programados en segundo plano |
| `StreakWorker.kt` | Worker | Verifica y actualiza la racha de estudio a medianoche |
| `WorkManagerScheduler.kt` | Scheduler | Programa y gestiona los workers de WorkManager |

#### 📦 Raíz del paquete

| Archivo/Clase | Tipo | Responsabilidad |
|:--------------|:-----|:----------------|
| `KotodamaApplication.kt` | Application | Inicializa Hilt y WorkManager, programa verificación de racha |
| `MainActivity.kt` | Activity | Activity principal, punto de entrada de la UI Compose |

---

### 🧭 Guía de Navegación del Código

#### ¿Dónde encontrar cada tipo de lógica?

| Si buscas... | Busca en... |
|:-------------|:------------|
| **Lógica de negocio pura** | `domain/` - Modelos, interfaces de repositorios y casos de uso |
| **Acceso a datos** | `data/` - Implementaciones de repositorios, Room Database, Firebase |
| **Interfaz de usuario** | `presentation/screens/` - Pantallas organizadas por feature |
| **Componentes reutilizables** | `presentation/components/` - Cards, badges y widgets |
| **Navegación** | `presentation/navigation/` - Rutas y grafo de navegación |
| **Inyección de dependencias** | `di/` - Módulos de Hilt |
| **Tareas en segundo plano** | `workers/` - WorkManager workers |

#### Flujo de datos típico (MVI Pattern)

```
Usuario interactúa → Screen → ViewModel → Repository → Data Source
                         ↑        ↓
                    State ← Effect/Event
```

1. **Screen** (Composable): Renderiza la UI basada en el State
2. **ViewModel**: Procesa eventos, actualiza el State, coordina con Repository
3. **Repository**: Abstrae el acceso a datos (Room + Firestore)
4. **Data Sources**: Room Database (local) y Firebase Firestore (nube)

#### Convenciones de nomenclatura

- **Screens**: `[Feature]Screen.kt` - Composable principal de la pantalla
- **States**: `[Feature]State.kt` - Data class con el estado de la UI
- **ViewModels**: `[Feature]ViewModel.kt` - Lógica de presentación
- **Events**: `[Feature]Event.kt` - Acciones del usuario (sealed class)
- **Effects**: `[Feature]Effect.kt` - Efectos secundarios one-shot (navegación, snackbars)

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
