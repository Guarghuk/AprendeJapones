package com.example.aprendejapones.di

import com.example.aprendejapones.data.repository.*
import com.example.aprendejapones.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.aprendejapones.data.local.preferences.PreferencesManager

/**
 * Módulo de Hilt para vincular interfaces de repositorio con sus implementaciones.
 *
 * Este módulo utiliza `@Binds` para conectar las interfaces de repositorio
 * del dominio con sus implementaciones concretas en la capa de datos,
 * siguiendo el principio de inversión de dependencias (DIP).
 *
 * ## Patrón Repository
 * El patrón Repository abstrae el acceso a datos, permitiendo que la capa
 * de dominio dependa de interfaces en lugar de implementaciones concretas.
 *
 * ## Repositorios Vinculados
 * - [UserRepository] → [UserRepositoryImpl]: Gestión de usuario local
 * - [ProgressRepository] → [ProgressRepositoryImpl]: Progreso de aprendizaje
 * - [AchievementRepository] → [AchievementRepositoryImpl]: Sistema de logros
 * - [LessonRepository] → [LessonRepositoryImpl]: Lecciones y desafíos
 * - [AuthRepository] → [AuthRepositoryImpl]: Autenticación
 * - [CommunityRepository] → [FirestoreCommunityRepositoryImpl]: Comunidad
 * - [FirestoreUserRepository] → [FirestoreUserRepositoryImpl]: Perfil en Firestore
 *
 * ## Uso
 *
 * Los repositorios se inyectan automáticamente donde se necesiten:
 *
 * ```kotlin
 * class HomeViewModel @Inject constructor(
 *     private val userRepository: UserRepository, // Se inyecta UserRepositoryImpl
 *     private val lessonRepository: LessonRepository // Se inyecta LessonRepositoryImpl
 * ) : ViewModel()
 * ```
 *
 * @see DatabaseModule Proporciona los DAOs usados por los repositorios.
 * @see FirebaseModule Proporciona los servicios de Firebase.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Vincula [UserRepository] con [UserRepositoryImpl].
     *
     * @param userRepositoryImpl Implementación del repositorio de usuario local.
     * @return La interfaz [UserRepository] implementada.
     */
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    /**
     * Vincula [ProgressRepository] con [ProgressRepositoryImpl].
     *
     * @param progressRepositoryImpl Implementación del repositorio de progreso.
     * @return La interfaz [ProgressRepository] implementada.
     */
    @Binds
    @Singleton
    abstract fun bindProgressRepository(
        progressRepositoryImpl: ProgressRepositoryImpl
    ): ProgressRepository

    /**
     * Vincula [AchievementRepository] con [AchievementRepositoryImpl].
     *
     * @param achievementRepositoryImpl Implementación del repositorio de logros.
     * @return La interfaz [AchievementRepository] implementada.
     */
    @Binds
    @Singleton
    abstract fun bindAchievementRepository(
        achievementRepositoryImpl: AchievementRepositoryImpl
    ): AchievementRepository

    /**
     * Vincula [LessonRepository] con [LessonRepositoryImpl].
     *
     * @param lessonRepositoryImpl Implementación del repositorio de lecciones.
     * @return La interfaz [LessonRepository] implementada.
     */
    @Binds
    @Singleton
    abstract fun bindLessonRepository(
        lessonRepositoryImpl: LessonRepositoryImpl
    ): LessonRepository

    /**
     * Vincula [AuthRepository] con [AuthRepositoryImpl].
     *
     * @param authRepositoryImpl Implementación del repositorio de autenticación.
     * @return La interfaz [AuthRepository] implementada.
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    /**
     * Vincula [CommunityRepository] con [FirestoreCommunityRepositoryImpl].
     *
     * @param firestoreCommunityRepositoryImpl Implementación del repositorio de comunidad.
     * @return La interfaz [CommunityRepository] implementada.
     */
    @Binds
    @Singleton
    abstract fun bindFirestoreCommunityRepository(
        firestoreCommunityRepositoryImpl: FirestoreCommunityRepositoryImpl
    ): CommunityRepository

    /**
     * Vincula [FirestoreUserRepository] con [FirestoreUserRepositoryImpl].
     *
     * @param firestoreUserRepositoryImpl Implementación del repositorio de usuario Firestore.
     * @return La interfaz [FirestoreUserRepository] implementada.
     */
    @Binds
    @Singleton
    abstract fun bindFirestoreUserRepository(
        firestoreUserRepositoryImpl: FirestoreUserRepositoryImpl
    ): FirestoreUserRepository
}