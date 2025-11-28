package com.example.aprendejapones.data.local.database.dao

import androidx.room.*
import com.example.aprendejapones.data.local.database.entity.HistorialLeccionesEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de Historial de Lecciones
 */
@Dao
interface HistorialLeccionesDao {

    @Query("SELECT * FROM historial_lecciones WHERE id_usuario = :idUsuario ORDER BY fecha_completado DESC")
    fun getHistorialUsuarioFlow(idUsuario: String): Flow<List<HistorialLeccionesEntity>>

    @Query("SELECT * FROM historial_lecciones WHERE id_usuario = :idUsuario ORDER BY fecha_completado DESC LIMIT :limit")
    suspend fun getHistorialReciente(idUsuario: String, limit: Int = 10): List<HistorialLeccionesEntity>

    @Query("SELECT * FROM historial_lecciones WHERE id_usuario = :idUsuario")
    suspend fun getAllHistorial(idUsuario: String): List<HistorialLeccionesEntity>

    @Query("SELECT COUNT(*) FROM historial_lecciones WHERE id_usuario = :idUsuario")
    suspend fun getTotalLeccionesCompletadas(idUsuario: String): Int

    @Query("SELECT SUM(tiempo_tardado_seg) FROM historial_lecciones WHERE id_usuario = :idUsuario")
    suspend fun getTotalTiempoEstudio(idUsuario: String): Int?

    @Query("SELECT SUM(xp_ganada) FROM historial_lecciones WHERE id_usuario = :idUsuario")
    suspend fun getTotalXPGanada(idUsuario: String): Int?

    @Insert
    suspend fun insertHistorial(historial: HistorialLeccionesEntity)

    @Update
    suspend fun updateHistorial(historial: HistorialLeccionesEntity)

    @Delete
    suspend fun deleteHistorial(historial: HistorialLeccionesEntity)

    @Query("DELETE FROM historial_lecciones WHERE id_usuario = :idUsuario")
    suspend fun deleteHistorialUsuario(idUsuario: String)
}
