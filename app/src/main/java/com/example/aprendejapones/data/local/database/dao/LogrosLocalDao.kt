package com.example.aprendejapones.data.local.database.dao

import androidx.room.*
import com.example.aprendejapones.data.local.database.entity.LogrosLocalEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de Logros Locales
 */
@Dao
interface LogrosLocalDao {

    @Query("SELECT * FROM logros_local WHERE id_usuario = :idUsuario ORDER BY fecha_obtencion DESC")
    fun getLogrosUsuarioFlow(idUsuario: String): Flow<List<LogrosLocalEntity>>

    @Query("SELECT * FROM logros_local WHERE id_usuario = :idUsuario")
    suspend fun getLogrosUsuario(idUsuario: String): List<LogrosLocalEntity>

    @Query("SELECT * FROM logros_local WHERE id_usuario = :idUsuario AND id_logro_definicion = :idLogroDefinicion")
    suspend fun getLogro(idUsuario: String, idLogroDefinicion: String): LogrosLocalEntity?

    @Query("SELECT COUNT(*) FROM logros_local WHERE id_usuario = :idUsuario")
    suspend fun getLogrosCount(idUsuario: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogro(logro: LogrosLocalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogros(logros: List<LogrosLocalEntity>)

    @Update
    suspend fun updateLogro(logro: LogrosLocalEntity)

    @Delete
    suspend fun deleteLogro(logro: LogrosLocalEntity)

    @Query("DELETE FROM logros_local WHERE id_usuario = :idUsuario")
    suspend fun deleteLogrosUsuario(idUsuario: String)
}
