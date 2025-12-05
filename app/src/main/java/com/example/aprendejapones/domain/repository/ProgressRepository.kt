package com.example.aprendejapones.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Modelo de datos que representa el progreso en una categoría de aprendizaje.
 *
 * Cada categoría (Hiragana, Katakana, Kanji, etc.) tiene su propio
 * registro de progreso que indica cuántos elementos ha aprendido
 * el usuario.
 *
 * @property category Nombre de la categoría (ej: "Hiragana", "Kanji").
 * @property progress Porcentaje de progreso (0-100).
 * @property itemsLearned Número de elementos aprendidos.
 * @property totalItems Número total de elementos en la categoría.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
data class CategoryProgress(
    val category: String,
    val progress: Int,
    val itemsLearned: Int,
    val totalItems: Int
)

/**
 * Repositorio para la gestión del progreso de aprendizaje.
 *
 * Esta interfaz define el contrato para las operaciones relacionadas
 * con el seguimiento del progreso del usuario en las diferentes
 * categorías de aprendizaje del japonés.
 *
 * ## Categorías de Progreso
 * - **Hiragana:** 46 caracteres básicos + variantes
 * - **Katakana:** 46 caracteres básicos + variantes
 * - **Kanji:** Kanji organizados por nivel JLPT
 * - **Vocabulario:** Palabras y expresiones
 * - **Gramática:** Reglas gramaticales
 *
 * @see CategoryProgress Modelo de progreso por categoría.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
interface ProgressRepository {

    /**
     * Obtiene el progreso de todas las categorías como Flow reactivo.
     *
     * Emite actualizaciones cuando cambia el progreso de cualquier
     * categoría, permitiendo actualizar la UI automáticamente.
     *
     * @return Flow que emite la lista de [CategoryProgress].
     */
    fun getUserProgressFlow(): Flow<List<CategoryProgress>>

    /**
     * Obtiene el progreso de todas las categorías de forma única.
     *
     * @return Lista de [CategoryProgress] con el estado actual.
     */
    suspend fun getAllProgress(): List<CategoryProgress>

    /**
     * Obtiene el progreso de una categoría específica.
     *
     * @param category Nombre de la categoría a consultar.
     * @return [CategoryProgress] de la categoría o `null` si no existe.
     */
    suspend fun getProgressByCategory(category: String): CategoryProgress?

    /**
     * Actualiza el progreso de una categoría.
     *
     * Se debe llamar cuando el usuario aprende nuevos elementos
     * o completa actividades en una categoría.
     *
     * @param category Nombre de la categoría a actualizar.
     * @param progressPercent Nuevo porcentaje de progreso (0-100).
     * @param itemsLearned Nuevo número de elementos aprendidos.
     */
    suspend fun updateProgress(
        category: String,
        progressPercent: Int,
        itemsLearned: Int
    )

    /**
     * Inicializa las categorías de progreso por defecto.
     *
     * Crea registros de progreso para todas las categorías con
     * valores iniciales de 0. Se debe llamar al crear un nuevo usuario.
     */
    suspend fun initializeDefaultProgress()
}