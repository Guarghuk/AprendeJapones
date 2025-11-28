package com.example.aprendejapones.data.repository

import com.example.aprendejapones.data.local.database.dao.LogrosLocalDao
import com.example.aprendejapones.data.local.database.dao.UsuariosLocalDao
import com.example.aprendejapones.data.local.database.entity.LogrosLocalEntity
import com.example.aprendejapones.domain.repository.AchievementRepository
import com.example.aprendejapones.utils.Achievement
import com.example.aprendejapones.utils.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRepositoryImpl @Inject constructor(
    private val logrosLocalDao: LogrosLocalDao,
    private val usuariosLocalDao: UsuariosLocalDao
) : AchievementRepository {

    // Cache of achievement definitions (from MockData)
    private val achievementDefinitions: List<Achievement> by lazy {
        MockData.getMockAchievements()
    }

    override fun getUserAchievementsFlow(): Flow<List<Achievement>> {
        return usuariosLocalDao.getCurrentUsuarioFlow().map { usuario ->
            usuario?.let {
                val unlockedLogros = logrosLocalDao.getLogrosUsuario(it.idUsuario)
                val unlockedIds = unlockedLogros.map { logro -> logro.idLogroDefinicion }.toSet()
                
                achievementDefinitions.map { achievement ->
                    achievement.copy(isUnlocked = unlockedIds.contains(achievement.id))
                }
            } ?: emptyList()
        }
    }

    override suspend fun getUserAchievements(): List<Achievement> {
        val usuario = usuariosLocalDao.getCurrentUsuario() ?: return emptyList()
        val unlockedLogros = logrosLocalDao.getLogrosUsuario(usuario.idUsuario)
        val unlockedIds = unlockedLogros.map { it.idLogroDefinicion }.toSet()
        
        return achievementDefinitions.map { achievement ->
            achievement.copy(isUnlocked = unlockedIds.contains(achievement.id))
        }
    }

    override suspend fun initializeDefaultAchievements() {
        // With the new schema, we don't need to initialize achievements
        // They are now stored only when unlocked
        // Achievement definitions come from MockData
    }

    override suspend fun unlockAchievement(achievementId: String) {
        val usuario = usuariosLocalDao.getCurrentUsuario() ?: return
        
        // Check if already unlocked
        val existing = logrosLocalDao.getLogro(usuario.idUsuario, achievementId)
        if (existing != null) return
        
        logrosLocalDao.insertLogro(
            LogrosLocalEntity(
                idUsuario = usuario.idUsuario,
                idLogroDefinicion = achievementId,
                fechaObtencion = System.currentTimeMillis()
            )
        )
    }

    override suspend fun updateAchievementProgress(achievementId: String, progress: Int) {
        // With the new schema, we only store unlocked achievements
        // Progress tracking would need a separate table or be calculated from lesson history
        // Auto-unlock if progress reaches 100%
        if (progress >= 100) {
            unlockAchievement(achievementId)
        }
    }

    override suspend fun checkAchievements() {
        val usuario = usuariosLocalDao.getCurrentUsuario() ?: return

        // Check "Dedicated Student" achievement (7 day streak)
        if (usuario.rachaDias >= 7) {
            unlockAchievement("2") // Dedicated Student
        }
        
        // Other achievement checks can be added here
    }
}
