package com.example.aprendejapones.presentation.screens.home

import com.example.aprendejapones.domain.model.DailyChallenge
import com.example.aprendejapones.domain.model.KitsuneMessage
import com.example.aprendejapones.domain.model.LessonFunction
import com.example.aprendejapones.domain.model.User

/**
 * Estado de la pantalla Home.
 *
 * Representa todo el estado de la UI necesario para renderizar la pantalla
 * principal de la aplicación, siguiendo el patrón MVI (Model-View-Intent).
 *
 * ## Propiedades de Estado
 * - [user]: Información del usuario actual
 * - [dailyChallenge]: Desafío diario activo
 * - [kitsuneMessage]: Mensaje de la mascota Kitsune
 * - [lessonFunctions]: Lecciones disponibles
 * - [isLoading]: Estado de carga
 * - [error]: Mensaje de error si hay alguno
 *
 * ## Propiedades Computadas
 * Proporciona acceso conveniente a propiedades del usuario con valores
 * por defecto para evitar null checks en la UI.
 *
 * @property user Modelo del usuario actual o null si no está cargado.
 * @property dailyChallenge Desafío diario activo o null.
 * @property kitsuneMessage Mensaje motivacional de Kitsune o null.
 * @property lessonFunctions Lista de lecciones disponibles.
 * @property isLoading Indica si hay una operación de carga en progreso.
 * @property error Mensaje de error para mostrar al usuario.
 *
 * @see HomeViewModel ViewModel que gestiona este estado.
 * @see HomeEvent Eventos que pueden modificar este estado.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
data class HomeState(
    val user: User? = null,
    val dailyChallenge: DailyChallenge? = null,
    val kitsuneMessage: KitsuneMessage? = null,
    val lessonFunctions: List<LessonFunction> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    // ============ Propiedades Computadas ============

    /**
     * Rango del usuario en japonés.
     * @return Rango del usuario o "初心者" (Principiante) por defecto.
     */
    val userRank: String
        get() = user?.rank ?: "初心者"

    /**
     * Días de racha de estudio.
     * @return Número de días de racha o 0 por defecto.
     */
    val streak: Int
        get() = user?.streak ?: 0

    /**
     * Monedas (drops) del usuario.
     * @return Cantidad de drops o 0 por defecto.
     */
    val drops: Int
        get() = user?.drops ?: 0
    
    /**
     * Nombre de usuario para mostrar.
     * @return Nombre del usuario o "Usuario" por defecto.
     */
    val username: String
        get() = user?.username ?: "Usuario"
    
    /**
     * Primera letra del nombre para el avatar.
     * @return Letra del avatar o "K" por defecto.
     */
    val avatarLetter: String
        get() = user?.avatarLetter ?: "K"
    
    /**
     * Nivel actual del usuario.
     * @return Nivel del usuario o 1 por defecto.
     */
    val level: Int
        get() = user?.level ?: 1
    
    /**
     * XP actual en el nivel.
     * @return XP actual o 0 por defecto.
     */
    val currentXP: Int
        get() = user?.currentXP ?: 0
    
    /**
     * XP necesario para el siguiente nivel.
     * @return XP máximo o 100 por defecto.
     */
    val maxXP: Int
        get() = user?.maxXP ?: 100

    /**
     * Indica si hay notificaciones pendientes.
     * @return `true` si hay notificaciones, `false` por defecto.
     */
    val hasNotifications: Boolean
        get() = false // TODO: Implementar lógica de notificaciones
}

/**
 * Eventos que pueden ocurrir en la pantalla Home.
 *
 * Representa todas las acciones del usuario que el ViewModel debe procesar.
 * Sigue el patrón MVI donde cada interacción del usuario se modela como un evento.
 *
 * @see HomeViewModel Procesa estos eventos.
 * @see HomeState Estado modificado por estos eventos.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
sealed class HomeEvent {
    /** Evento para cargar los datos iniciales */
    object LoadData : HomeEvent()

    /** Evento para refrescar los datos */
    object RefreshData : HomeEvent()

    /**
     * Evento cuando el usuario selecciona una función/lección.
     * @property functionName Nombre de la función seleccionada.
     */
    data class SelectFunction(val functionName: String) : HomeEvent()

    /** Evento para descartar el mensaje de error */
    object DismissError : HomeEvent()

    /** Evento cuando el usuario completa parte del desafío diario */
    object MarkChallengeProgress : HomeEvent()
}

/**
 * Efectos secundarios de la pantalla Home.
 *
 * Representa eventos de una sola vez (one-shot events) que no forman
 * parte del estado persistente, como navegación o mensajes toast.
 *
 * ## Diferencia con State
 * - **State:** Persistente, describe la UI actual
 * - **Effect:** Transitorio, se consume una sola vez
 *
 * @see HomeViewModel Emite estos efectos.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
sealed class HomeEffect {
    /**
     * Efecto para navegar a una lección específica.
     * @property functionName Nombre de la lección a la que navegar.
     */
    data class NavigateToLesson(val functionName: String) : HomeEffect()

    /**
     * Efecto para mostrar un mensaje toast.
     * @property message Mensaje a mostrar al usuario.
     */
    data class ShowToast(val message: String) : HomeEffect()
}