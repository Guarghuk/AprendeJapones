package com.example.aprendejapones.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para Usuario Local
 * Representa la tabla de usuarios en la base de datos local
 */
@Entity(tableName = "usuarios_local")
data class UsuariosLocalEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_usuario")
    val idUsuario: String,
    
    @ColumnInfo(name = "nombre_usuario")
    val nombreUsuario: String,
    
    @ColumnInfo(name = "email")
    val email: String,
    
    @ColumnInfo(name = "nivel")
    val nivel: Int,
    
    @ColumnInfo(name = "xp_actual")
    val xpActual: Int,
    
    @ColumnInfo(name = "xp_max_nivel")
    val xpMaxNivel: Int,
    
    @ColumnInfo(name = "racha_dias")
    val rachaDias: Int,
    
    @ColumnInfo(name = "monedas")
    val monedas: Int,
    
    @ColumnInfo(name = "fecha_registro")
    val fechaRegistro: Long,
    
    @ColumnInfo(name = "ultima_conexion")
    val ultimaConexion: Long
)
