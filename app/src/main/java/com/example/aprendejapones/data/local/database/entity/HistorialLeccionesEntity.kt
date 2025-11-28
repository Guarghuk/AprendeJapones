package com.example.aprendejapones.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad Room para el Historial de Lecciones
 * Representa la tabla de historial de lecciones completadas en la base de datos local
 */
@Entity(
    tableName = "historial_lecciones",
    foreignKeys = [
        ForeignKey(
            entity = UsuariosLocalEntity::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_usuario"])]
)
data class HistorialLeccionesEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_historial")
    val idHistorial: Int = 0,
    
    @ColumnInfo(name = "id_usuario")
    val idUsuario: String,
    
    @ColumnInfo(name = "id_leccion")
    val idLeccion: String,
    
    @ColumnInfo(name = "fecha_completado")
    val fechaCompletado: Long,
    
    @ColumnInfo(name = "respuestas_correctas")
    val respuestasCorrectas: Int,
    
    @ColumnInfo(name = "total_preguntas")
    val totalPreguntas: Int,
    
    @ColumnInfo(name = "xp_ganada")
    val xpGanada: Int,
    
    @ColumnInfo(name = "tiempo_tardado_seg")
    val tiempoTardadoSeg: Int
)
