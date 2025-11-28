package com.example.aprendejapones.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aprendejapones.data.local.database.dao.*
import com.example.aprendejapones.data.local.database.entity.*

/**
 * Base de datos principal de la aplicación
 * Version 2: Nuevo esquema híbrido Room + Firestore
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

    abstract fun usuariosLocalDao(): UsuariosLocalDao
    abstract fun progresoCategoriaDao(): ProgresoCategoriaDao
    abstract fun historialLeccionesDao(): HistorialLeccionesDao
    abstract fun logrosLocalDao(): LogrosLocalDao

    companion object {
        const val DATABASE_NAME = "kotodama_database"
    }
}