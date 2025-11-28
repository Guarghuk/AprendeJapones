package com.example.aprendejapones.data.repository

import com.example.aprendejapones.data.local.database.dao.ProgresoCategoriaDao
import com.example.aprendejapones.data.local.database.dao.UsuariosLocalDao
import com.example.aprendejapones.data.local.database.entity.ProgresoCategoriaEntity
import com.example.aprendejapones.domain.repository.CategoryProgress
import com.example.aprendejapones.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val progresoCategoriaDao: ProgresoCategoriaDao,
    private val usuariosLocalDao: UsuariosLocalDao
) : ProgressRepository {

    override fun getUserProgressFlow(): Flow<List<CategoryProgress>> {
        return usuariosLocalDao.getCurrentUsuarioFlow().map { usuario ->
            usuario?.let {
                progresoCategoriaDao.getAllProgreso(it.idUsuario).map { entity ->
                    CategoryProgress(
                        category = entity.idCategoria,
                        progress = entity.porcentajeCompletado,
                        itemsLearned = 0, // Not stored in new schema, calculate from other data
                        totalItems = getTotalItemsForCategory(entity.idCategoria)
                    )
                }
            } ?: emptyList()
        }
    }

    override suspend fun getAllProgress(): List<CategoryProgress> {
        val usuario = usuariosLocalDao.getCurrentUsuario() ?: return emptyList()
        return progresoCategoriaDao.getAllProgreso(usuario.idUsuario).map { entity ->
            CategoryProgress(
                category = entity.idCategoria,
                progress = entity.porcentajeCompletado,
                itemsLearned = 0, // Not stored in new schema
                totalItems = getTotalItemsForCategory(entity.idCategoria)
            )
        }
    }

    override suspend fun getProgressByCategory(category: String): CategoryProgress? {
        val usuario = usuariosLocalDao.getCurrentUsuario() ?: return null
        val entity = progresoCategoriaDao.getProgresoByCategoria(usuario.idUsuario, category) ?: return null

        return CategoryProgress(
            category = entity.idCategoria,
            progress = entity.porcentajeCompletado,
            itemsLearned = 0, // Not stored in new schema
            totalItems = getTotalItemsForCategory(entity.idCategoria)
        )
    }

    override suspend fun updateProgress(
        category: String,
        progressPercent: Int,
        itemsLearned: Int
    ) {
        val usuario = usuariosLocalDao.getCurrentUsuario() ?: return

        val existing = progresoCategoriaDao.getProgresoByCategoria(usuario.idUsuario, category)

        if (existing != null) {
            progresoCategoriaDao.updateProgresoByCategoria(
                idUsuario = usuario.idUsuario,
                idCategoria = category,
                porcentaje = progressPercent
            )
        } else {
            progresoCategoriaDao.insertProgreso(
                ProgresoCategoriaEntity(
                    idUsuario = usuario.idUsuario,
                    idCategoria = category,
                    porcentajeCompletado = progressPercent,
                    fechaUltimaActividad = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun initializeDefaultProgress() {
        val usuario = usuariosLocalDao.getCurrentUsuario() ?: return

        val categories = listOf("hiragana", "katakana", "kanji", "grammar", "vocabulary")

        categories.forEach { category ->
            val existing = progresoCategoriaDao.getProgresoByCategoria(usuario.idUsuario, category)
            if (existing == null) {
                progresoCategoriaDao.insertProgreso(
                    ProgresoCategoriaEntity(
                        idUsuario = usuario.idUsuario,
                        idCategoria = category,
                        porcentajeCompletado = 0,
                        fechaUltimaActividad = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    private fun getTotalItemsForCategory(category: String): Int {
        return when (category) {
            "hiragana" -> 46
            "katakana" -> 46
            "kanji" -> 2136
            "grammar" -> 100
            "vocabulary" -> 1000
            else -> 100
        }
    }
}
