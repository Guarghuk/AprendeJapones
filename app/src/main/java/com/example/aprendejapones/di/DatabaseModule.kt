package com.example.aprendejapones.di

import android.content.Context
import androidx.room.Room
import com.example.aprendejapones.data.local.database.AppDatabase
import com.example.aprendejapones.data.local.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para la configuración de Room Database.
 *
 * Este módulo proporciona la instancia de [AppDatabase] y todos los DAOs
 * necesarios para las operaciones de base de datos local. Utiliza el
 * patrón Singleton para asegurar una única instancia de la base de datos.
 *
 * ## Dependencias Proporcionadas
 * - [AppDatabase]: Base de datos principal de Room
 * - [UsuariosLocalDao]: DAO para operaciones de usuario
 * - [ProgresoCategoriaDao]: DAO para progreso por categoría
 * - [HistorialLeccionesDao]: DAO para historial de lecciones
 * - [LogrosLocalDao]: DAO para logros
 *
 * ## Configuración
 * - Usa `fallbackToDestructiveMigration()` para desarrollo
 * - **Nota:** En producción, se deben implementar migraciones apropiadas
 *
 * @see AppDatabase Base de datos que se configura en este módulo.
 * @see RepositoryModule Módulo que usa estos DAOs.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Proporciona la instancia singleton de [AppDatabase].
     *
     * Configura Room Database con:
     * - Nombre de archivo: "kotodama_database"
     * - Migración destructiva (solo para desarrollo)
     *
     * @param context Contexto de aplicación inyectado por Hilt.
     * @return Instancia configurada de [AppDatabase].
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // For development only
            .build()
    }

    /**
     * Proporciona el DAO para operaciones de usuarios locales.
     *
     * @param database Instancia de la base de datos.
     * @return [UsuariosLocalDao] para operaciones CRUD de usuarios.
     */
    @Provides
    @Singleton
    fun provideUsuariosLocalDao(database: AppDatabase): UsuariosLocalDao {
        return database.usuariosLocalDao()
    }

    /**
     * Proporciona el DAO para operaciones de progreso por categoría.
     *
     * @param database Instancia de la base de datos.
     * @return [ProgresoCategoriaDao] para operaciones de progreso.
     */
    @Provides
    @Singleton
    fun provideProgresoCategoriaDao(database: AppDatabase): ProgresoCategoriaDao {
        return database.progresoCategoriaDao()
    }

    /**
     * Proporciona el DAO para operaciones del historial de lecciones.
     *
     * @param database Instancia de la base de datos.
     * @return [HistorialLeccionesDao] para operaciones del historial.
     */
    @Provides
    @Singleton
    fun provideHistorialLeccionesDao(database: AppDatabase): HistorialLeccionesDao {
        return database.historialLeccionesDao()
    }

    /**
     * Proporciona el DAO para operaciones de logros locales.
     *
     * @param database Instancia de la base de datos.
     * @return [LogrosLocalDao] para operaciones de logros.
     */
    @Provides
    @Singleton
    fun provideLogrosLocalDao(database: AppDatabase): LogrosLocalDao {
        return database.logrosLocalDao()
    }
}
