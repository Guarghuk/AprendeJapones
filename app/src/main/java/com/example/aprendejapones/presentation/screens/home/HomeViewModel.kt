package com.example.aprendejapones.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.repository.AchievementRepository
import com.example.aprendejapones.domain.repository.LessonRepository
import com.example.aprendejapones.domain.repository.ProgressRepository
import com.example.aprendejapones.domain.repository.UserRepository
import com.example.aprendejapones.utils.MockData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel para la pantalla Home.
 *
 * Gestiona el estado de la pantalla principal de la aplicación siguiendo
 * el patrón MVI (Model-View-Intent). Coordina múltiples repositorios para
 * cargar y actualizar los datos del usuario, desafíos y lecciones.
 *
 * ## Arquitectura MVI
 * - **Model:** [HomeState] representa el estado completo de la UI
 * - **View:** Composables que observan [state]
 * - **Intent:** [HomeEvent] representa las acciones del usuario
 *
 * ## Flujos Reactivos
 * - [state]: StateFlow para el estado de la UI
 * - [effects]: SharedFlow para eventos de una sola vez
 *
 * ## Datos Cargados
 * - Usuario local con XP, nivel y racha
 * - Desafío diario con progreso
 * - Mensaje motivacional de Kitsune
 * - Lista de lecciones disponibles
 *
 * ## Uso
 *
 * ```kotlin
 * @Composable
 * fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
 *     val state by viewModel.state.collectAsState()
 *
 *     LaunchedEffect(Unit) {
 *         viewModel.effects.collect { effect ->
 *             when (effect) {
 *                 is HomeEffect.NavigateToLesson -> navigateToLesson(effect.functionName)
 *                 is HomeEffect.ShowToast -> showToast(effect.message)
 *             }
 *         }
 *     }
 *
 *     // UI based on state
 * }
 * ```
 *
 * @property userRepository Repositorio para datos del usuario local.
 * @property lessonRepository Repositorio para lecciones y desafíos.
 * @property achievementRepository Repositorio para logros.
 * @property progressRepository Repositorio para progreso de aprendizaje.
 *
 * @see HomeState Estado de la UI.
 * @see HomeEvent Eventos del usuario.
 * @see HomeEffect Efectos secundarios.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val lessonRepository: LessonRepository,
    private val achievementRepository: AchievementRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    /** Estado mutable interno de la UI */
    private val _state = MutableStateFlow(HomeState())

    /** Estado público e inmutable que observa la UI */
    val state: StateFlow<HomeState> = _state.asStateFlow()

    /** Flujo mutable de efectos secundarios */
    private val _effects = MutableSharedFlow<HomeEffect>()

    /** Efectos públicos para eventos de una sola vez */
    val effects: SharedFlow<HomeEffect> = _effects.asSharedFlow()

    init {
        initializeUser()
        loadInitialData()
    }

    /**
     * Inicializa o recupera el usuario existente.
     *
     * Si no existe un usuario en la base de datos, crea uno nuevo
     * con valores por defecto e inicializa el progreso y logros.
     */
    private fun initializeUser() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                // Get or create user with UUID
                val user = userRepository.getOrCreateUser()

                // Initialize default data
                progressRepository.initializeDefaultProgress()
                achievementRepository.initializeDefaultAchievements()
                lessonRepository.initializeTodayChallenge()
                withContext(Dispatchers.Main) { /* Update state on main */ }

            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Error initializing user: ${e.message}")
                }
            }
        }
    }

    /**
     * Carga todos los datos necesarios para la pantalla Home.
     *
     * Inicia la observación de flujos reactivos para el usuario y
     * el desafío diario, además de cargar datos estáticos como
     * el mensaje de Kitsune y las funciones de lección.
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // Collect user flow in parallel
                launch {
                    userRepository.getCurrentUserFlow()
                        .collect { user ->
                            _state.update { it.copy(user = user) }
                        }
                }

                // Collect daily challenge in parallel
                launch {
                    lessonRepository.getTodayChallengeFlow()
                        .collect { challenge ->
                            _state.update { it.copy(dailyChallenge = challenge) }
                        }
                }

                // Load static data (only once)
                val kitsuneMessage = MockData.getMockKitsuneMessage()
                val lessonFunctions = MockData.getMockLessonFunctions()

                _state.update {
                    it.copy(
                        kitsuneMessage = kitsuneMessage,
                        lessonFunctions = lessonFunctions,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error loading data: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Procesa los eventos de la UI.
     *
     * Punto de entrada para todas las acciones del usuario,
     * despachando cada evento a su handler correspondiente.
     *
     * @param event El evento a procesar.
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
     * Refresca los datos de la pantalla.
     *
     * Reinicializa el desafío diario y emite un toast de confirmación.
     */
    private fun refreshData() {
        viewModelScope.launch {
            try {
                lessonRepository.initializeTodayChallenge()
                _effects.emit(HomeEffect.ShowToast("Data refreshed"))
            } catch (e: Exception) {
                _effects.emit(HomeEffect.ShowToast("Error refreshing"))
            }
        }
    }

    /**
     * Maneja la selección de una función/lección.
     *
     * Emite un efecto de navegación hacia la lección seleccionada.
     *
     * @param functionName Nombre de la función seleccionada.
     */
    private fun handleFunctionSelection(functionName: String) {
        viewModelScope.launch {
            _effects.emit(HomeEffect.NavigateToLesson(functionName))
        }
    }

    /**
     * Actualiza el progreso del desafío diario.
     *
     * Incrementa el contador de progreso y, si se completa el desafío,
     * otorga las recompensas de XP y monedas correspondientes.
     */
    private fun updateChallengeProgress() {
        viewModelScope.launch {
            try {
                lessonRepository.updateChallengeProgress()

                val challenge = _state.value.dailyChallenge
                if (challenge?.isCompleted == true) {
                    // Award XP for completing daily challenge
                    userRepository.addXP(challenge.rewardXP)
                    // Award coins for completing daily challenge
                    userRepository.addDrops(challenge.rewardCoins)
                    _effects.emit(
                        HomeEffect.ShowToast("¡Desafío completado! +${challenge.rewardXP} XP, +${challenge.rewardCoins} monedas")
                    )
                }
            } catch (e: Exception) {
                _effects.emit(HomeEffect.ShowToast("Error updating challenge"))
            }
        }
    }

    /**
     * Descarta el mensaje de error actual.
     */
    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}