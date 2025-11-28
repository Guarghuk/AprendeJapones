package com.example.aprendejapones.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad Room para los Logros Locales
 * Representa la tabla de logros obtenidos por el usuario en la base de datos local
 */
@Entity(
    tableName = "logros_local",
    foreignKeys = [
        ForeignKey(
            entity = UsuariosLocalEntity::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_usuario", "id_logro_definicion"], unique = true)]
)
data class LogrosLocalEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_logro")
    val idLogro: Int = 0,
    
    @ColumnInfo(name = "id_usuario")
    val idUsuario: String,
    
    @ColumnInfo(name = "id_logro_definicion")
    val idLogroDefinicion: String,
    
    @ColumnInfo(name = "fecha_obtencion")
    val fechaObtencion: Long
)
