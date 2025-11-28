package com.example.aprendejapones.presentation.screens.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.SavedStateHandle
import com.example.aprendejapones.domain.manager.StreakManager
import com.example.aprendejapones.domain.repository.LessonContentRepository
import com.example.aprendejapones.domain.repository.LessonRepository
import com.example.aprendejapones.domain.repository.ProgressRepository
import com.example.aprendejapones.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para LessonScreen
 */

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val lessonContentRepository: LessonContentRepository,
    private val lessonRepository: LessonRepository,
    private val userRepository: UserRepository,
    private val streakManager: StreakManager,
    private val progressRepository: ProgressRepository
    //private val dailyChallengeEntity: DailyChallengeEntity
) : ViewModel() {
    private val CURRENT_QUESTION = "current_question_index"
    private val SELECTED_ANSWER = "selected_answer"
    private val CORRECT_ANSWERS = "correct_answers_count"
    private val _state = MutableStateFlow(LessonState())
    val state: StateFlow<LessonState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<LessonEffect>()
    val effects: SharedFlow<LessonEffect> = _effects.asSharedFlow()

    init {
        val restoredAnswer: String? = savedStateHandle.get<String>(SELECTED_ANSWER)
        val restoredCurrentQuestion: Int = savedStateHandle.get<Int>(CURRENT_QUESTION) ?: 1
        val restoredCorrectAnswers: Int = savedStateHandle.get<Int>(CORRECT_ANSWERS) ?: 0
        _state.update { currentState ->
            currentState.copy(
                selectedAnswer = restoredAnswer,
                currentQuestion = restoredCurrentQuestion,
                correctAnswers = restoredCorrectAnswers,
                // Asegúrate de restaurar todas las variables que necesitas
            )
        }
        // Opcional: Si hay carga inicial de DB, hazla aquí y override con restored
    }
    fun onEvent(event: LessonEvent) {
        when (event) {
            is LessonEvent.LoadLesson -> loadLesson(event.functionName)
            is LessonEvent.SelectAnswer -> selectAnswer(event.answer)
            is LessonEvent.VerifyAnswer -> verifyAnswer()
            is LessonEvent.NextQuestion -> nextQuestion()
            is LessonEvent.PreviousQuestion -> previousQuestion()
            is LessonEvent.FinishLesson -> finishLesson()
            is LessonEvent.RestartLesson -> restartLesson()
        }
    }

    private fun loadLesson(functionName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                // Obtener preguntas del repositorio
                val questions = lessonContentRepository.getQuestionsForLesson(functionName)

                _state.update {
                    it.copy(
                        functionName = functionName,
                        questions = questions,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        questions = emptyList()
                    )
                }
            }
        }
    }

    private fun selectAnswer(answer: String) {
        _state.update { it.copy(selectedAnswer = answer) }
        savedStateHandle[SELECTED_ANSWER ] = answer
    }

    private fun verifyAnswer() {
        val current = _state.value
        val currentQuestion = current.questions.getOrNull(current.currentQuestion - 1)

        if (currentQuestion != null && current.selectedAnswer != null) {
            val isCorrect = current.selectedAnswer == currentQuestion.correctAnswer
            val newCorrectAnswers = if (isCorrect) current.correctAnswers + 1 else current.correctAnswers
            _state.update {
                it.copy(
                    isAnswered = true,
                    correctAnswers = newCorrectAnswers,
                )

            }
            savedStateHandle[CORRECT_ANSWERS] = newCorrectAnswers
            viewModelScope.launch {
                _effects.emit(
                    LessonEffect.ShowToast(
                        if (isCorrect) "¡Correcto! ✓" else "Incorrecto ✗"
                    )
                )
            }
        }
    }

    private fun nextQuestion() {
        val current = _state.value

        if (current.currentQuestion < current.totalQuestions) {
            val nextIndex = current.currentQuestion +1
            _state.update {
                it.copy(
                    currentQuestion = nextIndex,
                    selectedAnswer = null,
                    isAnswered = false
                )
            }
            savedStateHandle[CURRENT_QUESTION] = nextIndex
        } else {
            finishLesson()
        }
    }

    private fun previousQuestion() {
        val current = _state.value

        if (current.currentQuestion > 1) {
            val nextIndex = current.currentQuestion +1
            _state.update {
                it.copy(
                    currentQuestion = nextIndex,
                    selectedAnswer = null,
                    isAnswered = false
                )
            }
            savedStateHandle[CURRENT_QUESTION] = nextIndex
        }
    }

    companion object {
        // Reward constants
        private const val XP_PER_CORRECT_ANSWER = 10
        private const val BASE_COINS = 5
        private const val BONUS_COINS_PERFECT = 10    // 100% correct
        private const val BONUS_COINS_EXCELLENT = 5   // >=80% correct
        private const val BONUS_COINS_GOOD = 2        // >=60% correct
        private const val THRESHOLD_PERFECT = 1.0f
        private const val THRESHOLD_EXCELLENT = 0.8f
        private const val THRESHOLD_GOOD = 0.6f
    }
    
    private fun finishLesson() {
        viewModelScope.launch {
            val state = _state.value

            try {
                // Calcular XP ganado
                val xpEarned = state.correctAnswers * XP_PER_CORRECT_ANSWER
                
                // Calcular monedas ganadas (basado en rendimiento)
                val coinsEarned = calculateCoinsEarned(state.correctAnswers, state.totalQuestions)

                // Guardar lección completada
                lessonRepository.saveLesson(
                    lessonType = state.functionName,
                    lessonName = state.functionName,
                    totalQuestions = state.totalQuestions,
                    correctAnswers = state.correctAnswers,
                    xpEarned = xpEarned,
                    timeSpentSeconds = 0
                )

                // Actualizar XP
                userRepository.addXP(xpEarned)
                
                // Actualizar monedas (drops) - always positive for valid lessons
                userRepository.addDrops(coinsEarned)

                // Actualizar racha
                streakManager.checkAndUpdateStreak()

                // ✅ Actualizar progreso por categoría
                updateCategoryProgress(state.functionName, state.correctAnswers, state.totalQuestions)

                // Actualizar desafío diario
                lessonRepository.updateChallengeProgress()

                // Mostrar resultados con recompensas
                _state.update { 
                    it.copy(
                        showResults = true,
                        xpEarned = xpEarned,
                        coinsEarned = coinsEarned
                    ) 
                }
                
                // Mostrar mensaje de recompensas
                _effects.emit(LessonEffect.ShowToast("+$xpEarned XP, +$coinsEarned 💧"))

            } catch (e: Exception) {
                _effects.emit(LessonEffect.ShowToast("Error al guardar progreso"))
            }
        }
    }
    
    /**
     * Calcula las monedas ganadas basándose en el rendimiento
     * Base: BASE_COINS monedas, bonus por respuestas correctas
     */
    private fun calculateCoinsEarned(correctAnswers: Int, totalQuestions: Int): Int {
        if (totalQuestions == 0) return 0
        val percentage = correctAnswers.toFloat() / totalQuestions
        val bonusCoins = when {
            percentage >= THRESHOLD_PERFECT -> BONUS_COINS_PERFECT
            percentage >= THRESHOLD_EXCELLENT -> BONUS_COINS_EXCELLENT
            percentage >= THRESHOLD_GOOD -> BONUS_COINS_GOOD
            else -> 0
        }
        return BASE_COINS + bonusCoins
    }

    private suspend fun updateCategoryProgress(
        lessonName: String,
        correctAnswers: Int,
        totalQuestions: Int
    ) {
        // Mapear nombre de lección a categoría
        val category = when (lessonName) {
            "Hiragana" -> "hiragana"
            "Katakana" -> "katakana"
            "Kanji", "漢" -> "kanji"
            "Gramática", "📖" -> "grammar"
            "Vocabulario", "Haz Frases", "Conversación" -> "vocabulary"
            else -> return // No actualizar si no es una categoría conocida
        }

        // Obtener progreso actual
        val currentProgress = progressRepository.getProgressByCategory(category)

        // Calcular nuevo progreso (incrementar basado en respuestas correctas)
        val progressIncrement = (correctAnswers.toFloat() / totalQuestions * 5).toInt() // 5% max por lección
        val newProgress = ((currentProgress?.progress ?: 0) + progressIncrement).coerceIn(0, 100)
        val newItemsLearned = (currentProgress?.itemsLearned ?: 0) + correctAnswers

        // Guardar progreso actualizado
        progressRepository.updateProgress(
            category = category,
            progressPercent = newProgress,
            itemsLearned = newItemsLearned
        )
    }


    private fun restartLesson() {
        _state.update {
            it.copy(
                currentQuestion = 1,
                selectedAnswer = null,
                correctAnswers = 0,
                isAnswered = false,
                showResults = false
            )
        }
    }
}