package com.example.aprendejapones.data.local.database.dao

import androidx.room.*
import com.example.aprendejapones.data.local.database.entity.ProgresoCategoriaEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de Progreso por Categoría
 */
@Dao
interface ProgresoCategoriaDao {

    @Query("SELECT * FROM progreso_categoria WHERE id_usuario = :idUsuario")
    fun getProgresoUsuarioFlow(idUsuario: String): Flow<List<ProgresoCategoriaEntity>>

    @Query("SELECT * FROM progreso_categoria WHERE id_usuario = :idUsuario AND id_categoria = :idCategoria")
    suspend fun getProgresoByCategoria(idUsuario: String, idCategoria: String): ProgresoCategoriaEntity?

    @Query("SELECT * FROM progreso_categoria WHERE id_usuario = :idUsuario")
    suspend fun getAllProgreso(idUsuario: String): List<ProgresoCategoriaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgreso(progreso: ProgresoCategoriaEntity)

    @Update
    suspend fun updateProgreso(progreso: ProgresoCategoriaEntity)

    @Query("""
        UPDATE progreso_categoria 
        SET porcentaje_completado = :porcentaje,
            fecha_ultima_actividad = :timestamp
        WHERE id_usuario = :idUsuario AND id_categoria = :idCategoria
    """)
    suspend fun updateProgresoByCategoria(
        idUsuario: String,
        idCategoria: String,
        porcentaje: Int,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("DELETE FROM progreso_categoria WHERE id_usuario = :idUsuario")
    suspend fun deleteProgresoUsuario(idUsuario: String)

    @Transaction
    suspend fun upsertProgreso(progreso: ProgresoCategoriaEntity) {
        val existing = getProgresoByCategoria(progreso.idUsuario, progreso.idCategoria)
        if (existing != null) {
            updateProgreso(progreso.copy(idProgreso = existing.idProgreso))
        } else {
            insertProgreso(progreso)
        }
    }
}
