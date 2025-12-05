package com.example.aprendejapones.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/** Extensión para obtener DataStore de preferencias desde el contexto */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kotodama_preferences")

/**
 * Módulo principal de Hilt para dependencias de la aplicación.
 *
 * Este módulo proporciona dependencias de nivel de aplicación que no
 * encajan en módulos más específicos como [DatabaseModule] o [FirebaseModule].
 *
 * ## Dependencias Proporcionadas
 * - [DataStore]: Almacenamiento de preferencias con DataStore
 * - [CoroutineScope]: Scope de coroutines a nivel de aplicación
 *
 * ## DataStore
 * - Nombre del archivo: "kotodama_preferences"
 * - Ubicación: `/data/data/<package>/files/datastore/`
 * - Thread-safe y reactivo con Flow
 *
 * ## Application Scope
 * - Usa [SupervisorJob] para que fallos en una coroutine no cancelen las demás
 * - Útil para operaciones que deben sobrevivir a cambios de configuración
 *
 * @see com.example.aprendejapones.data.local.preferences.PreferencesManager Usa el DataStore.
 * @see DatabaseModule Para configuración de Room.
 * @see FirebaseModule Para configuración de Firebase.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Proporciona la instancia de DataStore para preferencias.
     *
     * @param context Contexto de aplicación inyectado por Hilt.
     * @return [DataStore] de preferencias para la aplicación.
     */
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }

    /**
     * Proporciona un CoroutineScope a nivel de aplicación.
     *
     * Este scope sobrevive durante toda la vida de la aplicación y
     * usa [SupervisorJob] para aislamiento de errores entre coroutines.
     *
     * **Uso típico:** Operaciones de background que no dependen del lifecycle
     * de ningún componente específico.
     *
     * @return [CoroutineScope] para operaciones de larga duración.
     */
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }
}