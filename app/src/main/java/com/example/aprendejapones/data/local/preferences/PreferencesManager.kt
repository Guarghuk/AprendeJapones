package com.example.aprendejapones.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Manager para la gestión de preferencias de usuario con DataStore.
 *
 * Esta clase centraliza el acceso a las preferencias de la aplicación
 * utilizando Jetpack DataStore, proporcionando una API type-safe y
 * reactiva para leer y escribir configuraciones.
 *
 * ## Preferencias Soportadas
 * - **Onboarding:** Estado de visualización de la introducción
 * - **First Launch:** Indicador de primer lanzamiento de la app
 * - **User Name:** Nombre del usuario almacenado localmente
 *
 * ## Características
 * - Todas las operaciones de lectura son reactivas mediante [Flow]
 * - Las escrituras son suspending functions para uso con coroutines
 * - Thread-safe por diseño de DataStore
 *
 * ## Uso
 *
 * ```kotlin
 * // Observar preferencia
 * preferencesManager.hasSeenOnboarding.collect { seen ->
 *     if (!seen) navigateToOnboarding()
 * }
 *
 * // Guardar preferencia
 * preferencesManager.setOnboardingCompleted()
 * ```
 *
 * @property dataStore Instancia de DataStore inyectada por Hilt.
 *
 * @see com.example.aprendejapones.di.AppModule Para la configuración de DataStore.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Singleton
class PreferencesManager @Inject constructor(
    val dataStore: DataStore<Preferences>
) {
    companion object {
        /** Clave para el estado de visualización del onboarding */
        private val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")

        /** Clave para el nombre del usuario */
        private val USER_NAME = stringPreferencesKey("user_name")

        /** Clave para indicar si es el primer lanzamiento */
        private val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
    }

    // ============ Onboarding ============

    /**
     * Flow que indica si el usuario ya vio la pantalla de onboarding.
     *
     * Emite `false` si nunca ha visto el onboarding, `true` si ya lo completó.
     */
    val hasSeenOnboarding: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[HAS_SEEN_ONBOARDING] ?: false
    }

    /**
     * Marca el onboarding como completado.
     *
     * Después de llamar este método, [hasSeenOnboarding] emitirá `true`.
     */
    suspend fun setOnboardingCompleted() {
        dataStore.edit { preferences ->
            preferences[HAS_SEEN_ONBOARDING] = true
        }
    }

    // ============ First Launch ============

    /**
     * Flow que indica si es el primer lanzamiento de la aplicación.
     *
     * Emite `true` en el primer lanzamiento, `false` en lanzamientos posteriores.
     * Útil para inicializar datos por defecto o mostrar tutoriales.
     */
    val isFirstLaunch: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_FIRST_LAUNCH] ?: true
    }

    /**
     * Marca el primer lanzamiento como completado.
     *
     * Después de llamar este método, [isFirstLaunch] emitirá `false`.
     */
    suspend fun setFirstLaunchComplete() {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_LAUNCH] = false
        }
    }

    // ============ User Name ============

    /**
     * Flow que emite el nombre del usuario almacenado localmente.
     *
     * Emite `null` si no se ha guardado ningún nombre.
     */
    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_NAME]
    }

    /**
     * Guarda el nombre del usuario localmente.
     *
     * @param name Nombre del usuario a guardar.
     */
    suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }

    // ============ Clear All ============

    /**
     * Elimina todas las preferencias almacenadas.
     *
     * Útil para reset de la aplicación o logout completo.
     * **Advertencia:** Esta operación es irreversible.
     */
    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}