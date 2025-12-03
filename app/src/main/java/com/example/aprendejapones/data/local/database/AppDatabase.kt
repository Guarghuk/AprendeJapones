package com.example.aprendejapones.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aprendejapones.data.local.database.dao.*
import com.example.aprendejapones.data.local.database.entity.*

/**
 * Base de datos principal de la aplicación Kotodama.
 *
 * Esta clase define la configuración de Room Database para el almacenamiento
 * local de datos. Utiliza un esquema híbrido donde los datos principales
 * se sincronizan con Firestore mientras se mantiene una copia local para
 * acceso offline.
 *
 * ## Entidades
 * - [UsuariosLocalEntity]: Datos del perfil de usuario local
 * - [ProgresoCategoriaEntity]: Progreso por categoría de aprendizaje
 * - [HistorialLeccionesEntity]: Historial de lecciones completadas
 * - [LogrosLocalEntity]: Logros/achievements del usuario
 *
 * ## Versiones
 * - **Versión 1:** Esquema inicial
 * - **Versión 2:** Esquema híbrido Room + Firestore (actual)
 *
 * ## Uso
 *
 * La base de datos se proporciona mediante Hilt en [DatabaseModule]:
 *
 * ```kotlin
 * @Provides
 * @Singleton
 * fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
 *     return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
 *         .fallbackToDestructiveMigration()
 *         .build()
 * }
 * ```
 *
 * @see UsuariosLocalDao DAO para operaciones de usuario.
 * @see ProgresoCategoriaDao DAO para operaciones de progreso.
 * @see HistorialLeccionesDao DAO para operaciones del historial.
 * @see LogrosLocalDao DAO para operaciones de logros.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Database(
    entities = [
        UsuariosLocalEntity::class,
        ProgresoCategoriaEntity::class,
        HistorialLeccionesEntity::class,
        LogrosLocalEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Proporciona acceso al DAO de usuarios locales.
     *
     * @return [UsuariosLocalDao] para operaciones CRUD de usuarios.
     */
    abstract fun usuariosLocalDao(): UsuariosLocalDao

    /**
     * Proporciona acceso al DAO de progreso por categoría.
     *
     * @return [ProgresoCategoriaDao] para operaciones de progreso.
     */
    abstract fun progresoCategoriaDao(): ProgresoCategoriaDao

    /**
     * Proporciona acceso al DAO del historial de lecciones.
     *
     * @return [HistorialLeccionesDao] para operaciones del historial.
     */
    abstract fun historialLeccionesDao(): HistorialLeccionesDao

    /**
     * Proporciona acceso al DAO de logros locales.
     *
     * @return [LogrosLocalDao] para operaciones de logros.
     */
    abstract fun logrosLocalDao(): LogrosLocalDao

    companion object {
        /** Nombre del archivo de la base de datos SQLite */
        const val DATABASE_NAME = "kotodama_database"
    }
}