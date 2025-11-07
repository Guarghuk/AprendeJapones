package com.example.aprendejapones.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * ViewModel para la pantalla Home
 * Maneja la lógica de presentación y el estado de la UI
 *
 * En una implementación completa, inyectarías UseCases aquí:
 * @HiltViewModel
 * class HomeViewModel @Inject constructor(
 *     private val getUserUseCase: GetUserUseCase,
 *     private val getDailyChallengeUseCase: GetDailyChallengeUseCase,
 *     private val getLessonFunctionsUseCase: GetLessonFunctionsUseCase
 * ) : ViewModel()
 */
class HomeViewModel : ViewModel() {

    // Estado privado mutable
    private val _state = MutableStateFlow(HomeState())
    // Estado público inmutable
    val state: StateFlow<HomeState> = _state.asStateFlow()

    // Canal para efectos secundarios (one-time events)
    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects: SharedFlow<HomeEffect> = _effects.asSharedFlow()

    init {
        loadInitialData()
    }

    /**
     * Maneja todos los eventos de la UI
     */
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadData -> loadInitialData()
            is HomeEvent.RefreshData -> refreshData()
            is HomeEvent.SelectFunction -> handleFunctionSelection(event.functionName)
            is HomeEvent.DismissError -> dismissError()
            is HomeEvent.MarkChallengeProgress -> updateChallengeProgress()
        }
    }

    /**
     * Carga los datos iniciales
     * En producción, esto llamaría a UseCases
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // Simular carga de datos (reemplazar con UseCases reales)
                delay(500)

                val user = getMockUser()
                val challenge = getMockDailyChallenge()
                val message = getMockKitsuneMessage()
                val functions = getMockLessonFunctions()

                _state.update {
                    it.copy(
                        user = user,
                        dailyChallenge = challenge,
                        kitsuneMessage = message,
                        lessonFunctions = functions,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar los datos: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Refresca los datos
     */
    private fun refreshData() {
        viewModelScope.launch {
            try {
                val user = getMockUser()
                val challenge = getMockDailyChallenge()

                _state.update {
                    it.copy(
                        user = user,
                        dailyChallenge = challenge
                    )
                }

                _effects.emit(HomeEffect.ShowToast("Datos actualizados"))
            } catch (e: Exception) {
                _effects.emit(HomeEffect.ShowToast("Error al actualizar"))
            }
        }
    }

    /**
     * Maneja la selección de una función
     */
    private fun handleFunctionSelection(functionName: String) {
        viewModelScope.launch {
            _effects.emit(HomeEffect.NavigateToLesson(functionName))
        }
    }

    /**
     * Actualiza el progreso del desafío diario
     */
    private fun updateChallengeProgress() {
        viewModelScope.launch {
            val currentChallenge = _state.value.dailyChallenge ?: return@launch

            if (currentChallenge.completed < currentChallenge.total) {
                val updatedChallenge = currentChallenge.copy(
                    completed = currentChallenge.completed + 1
                )

                _state.update {
                    it.copy(dailyChallenge = updatedChallenge)
                }

                if (updatedChallenge.isCompleted) {
                    _effects.emit(
                        HomeEffect.ShowToast("¡Desafío completado! +${updatedChallenge.rewardXP} XP")
                    )
                }
            }
        }
    }

    /**
     * Descarta el error actual
     */
    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    // ========== MOCK DATA (Reemplazar con Repository/UseCases reales) ==========

    private fun getMockUser() = User(
        id = "user_123",
        username = "Usuario123",
        rank = "初心者",
        level = 8,
        currentXP = 1450,
        maxXP = 2000,
        streak = 7,
        drops = 150,
        memberSince = "Enero 2025"
    )

    private fun getMockDailyChallenge() = DailyChallenge(
        id = "challenge_today",
        completed = 3,
        total = 5,
        timeRemaining = "23:45:12",
        rewardXP = 50
    )

    private fun getMockKitsuneMessage() = KitsuneMessage(
        message = "¡Buenos días! Hoy es perfecto para practicar."
    )

    private fun getMockLessonFunctions() = listOf(
        LessonFunction("1", "💬", "Haz Frases", "Nuevas palabras"),
        LessonFunction("2", "📚", "Vocabulario", "Palabras esenciales"),
        LessonFunction("3", "あ", "Hiragana", "Sistema silábico"),
        LessonFunction("4", "ア", "Katakana", "Palabras extranjeras"),
        LessonFunction("5", "漢", "Kanji", "Caracteres japoneses"),
        LessonFunction("6", "🗣️", "Conversación", "Habla con IA"),
        LessonFunction("7", "🎤", "Pronunciación", "Escucha y repite"),
        LessonFunction("8", "📖", "Gramática", "Estructuras y partículas")
    )
}