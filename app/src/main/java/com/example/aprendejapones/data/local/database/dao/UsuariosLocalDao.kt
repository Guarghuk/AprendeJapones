package com.example.aprendejapones.data.local.database.dao

import androidx.room.*
import com.example.aprendejapones.data.local.database.entity.UsuariosLocalEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de Usuario Local
 */
@Dao
interface UsuariosLocalDao {

    @Query("SELECT * FROM usuarios_local WHERE id_usuario = :idUsuario")
    fun getUsuarioFlow(idUsuario: String): Flow<UsuariosLocalEntity?>

    @Query("SELECT * FROM usuarios_local LIMIT 1")
    fun getCurrentUsuarioFlow(): Flow<UsuariosLocalEntity?>

    @Query("SELECT * FROM usuarios_local WHERE id_usuario = :idUsuario")
    suspend fun getUsuarioById(idUsuario: String): UsuariosLocalEntity?

    @Query("SELECT * FROM usuarios_local LIMIT 1")
    suspend fun getCurrentUsuario(): UsuariosLocalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: UsuariosLocalEntity)

    @Update
    suspend fun updateUsuario(usuario: UsuariosLocalEntity)

    @Query("""
        UPDATE usuarios_local 
        SET xp_actual = :xp, 
            nivel = :nivel, 
            ultima_conexion = :timestamp 
        WHERE id_usuario = :idUsuario
    """)
    suspend fun updateXP(idUsuario: String, xp: Int, nivel: Int, timestamp: Long = System.currentTimeMillis())

    @Query("""
        UPDATE usuarios_local 
        SET racha_dias = :racha, 
            ultima_conexion = :timestamp 
        WHERE id_usuario = :idUsuario
    """)
    suspend fun updateRacha(idUsuario: String, racha: Int, timestamp: Long = System.currentTimeMillis())

    @Query("""
        UPDATE usuarios_local 
        SET monedas = monedas + :cantidad, 
            ultima_conexion = :timestamp 
        WHERE id_usuario = :idUsuario
    """)
    suspend fun addMonedas(idUsuario: String, cantidad: Int, timestamp: Long = System.currentTimeMillis())

    @Query("""
        UPDATE usuarios_local 
        SET monedas = monedas - :cantidad, 
            ultima_conexion = :timestamp 
        WHERE id_usuario = :idUsuario AND monedas >= :cantidad
    """)
    suspend fun spendMonedas(idUsuario: String, cantidad: Int, timestamp: Long = System.currentTimeMillis()): Int

    @Delete
    suspend fun deleteUsuario(usuario: UsuariosLocalEntity)

    @Query("DELETE FROM usuarios_local")
    suspend fun deleteAllUsuarios()
}
