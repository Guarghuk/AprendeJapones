package com.example.aprendejapones.data.repository

import com.benasher44.uuid.uuid4
import com.example.aprendejapones.data.local.database.dao.UsuariosLocalDao
import com.example.aprendejapones.data.local.database.entity.UsuariosLocalEntity
import com.example.aprendejapones.domain.model.User
import com.example.aprendejapones.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val usuariosLocalDao: UsuariosLocalDao
) : UserRepository {

    private val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("es", "ES"))

    companion object {
        // Default values for new users
        private const val DEFAULT_USERNAME = "Usuario"
        private const val DEFAULT_EMAIL = ""
        private const val DEFAULT_LEVEL = 1
        private const val DEFAULT_XP = 0
        private const val DEFAULT_MAX_XP = 100
        private const val DEFAULT_STREAK = 0
        private const val DEFAULT_COINS = 0
        private const val DEFAULT_RANK = "初心者" // Beginner
        private const val DEFAULT_AVATAR = "K"
        private const val XP_PER_LEVEL_INCREMENT = 50
    }

    override fun getCurrentUserFlow(): Flow<User?> {
        return usuariosLocalDao.getCurrentUsuarioFlow().map { it?.toDomain() }
    }

    override suspend fun getCurrentUser(): User? {
        return usuariosLocalDao.getCurrentUsuario()?.toDomain()
    }

    override suspend fun getOrCreateUser(): User {
        val existing = usuariosLocalDao.getCurrentUsuario()

        return if (existing != null) {
            existing.toDomain()
        } else {
            // Create new local user with UUID
            val now = System.currentTimeMillis()
            val newUserEntity = UsuariosLocalEntity(
                idUsuario = uuid4().toString(),
                nombreUsuario = DEFAULT_USERNAME,
                email = DEFAULT_EMAIL,
                nivel = DEFAULT_LEVEL,
                xpActual = DEFAULT_XP,
                xpMaxNivel = DEFAULT_MAX_XP,
                rachaDias = DEFAULT_STREAK,
                monedas = DEFAULT_COINS,
                fechaRegistro = now,
                ultimaConexion = now
            )

            usuariosLocalDao.insertUsuario(newUserEntity)
            newUserEntity.toDomain()
        }
    }

    override suspend fun updateUser(user: User) {
        val entity = user.toEntity()
        usuariosLocalDao.updateUsuario(entity)
    }

    override suspend fun addXP(xp: Int): User {
        val user = getCurrentUser() ?: throw IllegalStateException("No user found")

        var newXP = user.currentXP + xp
        var newLevel = user.level
        var newMaxXP = user.maxXP

        // Level up logic
        while (newXP >= newMaxXP) {
            newXP -= newMaxXP
            newLevel++
            newMaxXP = calculateMaxXP(newLevel)
        }

        usuariosLocalDao.updateXP(user.id, newXP, newLevel)

        return user.copy(
            currentXP = newXP,
            level = newLevel,
            maxXP = newMaxXP
        )
    }

    override suspend fun updateStreak(streak: Int) {
        val user = getCurrentUser() ?: return
        usuariosLocalDao.updateRacha(user.id, streak)
    }

    override suspend fun addDrops(amount: Int) {
        val user = getCurrentUser() ?: return
        usuariosLocalDao.addMonedas(user.id, amount)
    }

    override suspend fun spendDrops(amount: Int): Boolean {
        val user = getCurrentUser() ?: return false
        val rowsAffected = usuariosLocalDao.spendMonedas(user.id, amount)
        return rowsAffected > 0
    }

    private fun calculateMaxXP(level: Int): Int {
        return DEFAULT_MAX_XP + (level - 1) * XP_PER_LEVEL_INCREMENT
    }

    // Mapper functions
    private fun UsuariosLocalEntity.toDomain(): User {
        return User(
            id = idUsuario,
            username = nombreUsuario,
            rank = DEFAULT_RANK, // Default rank, could be calculated from level
            level = nivel,
            currentXP = xpActual,
            maxXP = xpMaxNivel,
            streak = rachaDias,
            drops = monedas,
            memberSince = dateFormat.format(Date(fechaRegistro)),
            avatarLetter = nombreUsuario.firstOrNull()?.toString() ?: DEFAULT_AVATAR
        )
    }

    private fun User.toEntity(): UsuariosLocalEntity {
        return UsuariosLocalEntity(
            idUsuario = id,
            nombreUsuario = username,
            email = "", // Email not stored in User domain model
            nivel = level,
            xpActual = currentXP,
            xpMaxNivel = maxXP,
            rachaDias = streak,
            monedas = drops,
            fechaRegistro = System.currentTimeMillis(), // Will be overwritten if existing
            ultimaConexion = System.currentTimeMillis()
        )
    }
}