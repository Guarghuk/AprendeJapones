package com.example.aprendejapones.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad Room para el Progreso por Categoría
 * Representa la tabla de progreso de categorías en la base de datos local
 */
@Entity(
    tableName = "progreso_categoria",
    foreignKeys = [
        ForeignKey(
            entity = UsuariosLocalEntity::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_usuario", "id_categoria"], unique = true)]
)
data class ProgresoCategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_progreso")
    val idProgreso: Int = 0,
    
    @ColumnInfo(name = "id_usuario")
    val idUsuario: String,
    
    @ColumnInfo(name = "id_categoria")
    val idCategoria: String,
    
    @ColumnInfo(name = "porcentaje_completado")
    val porcentajeCompletado: Int,
    
    @ColumnInfo(name = "fecha_ultima_actividad")
    val fechaUltimaActividad: Long
)
