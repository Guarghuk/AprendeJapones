package com.example.aprendejapones.presentation.screens.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.SavedStateHandle

/**
 * ViewModel para LessonScreen
 */
class LessonViewModel(
    private val savedStateHandle: SavedStateHandle
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
            delay(300)

            // Mock questions
            val questions = listOf(
                Question(
                    "1",
                    "¿Qué significa este kanji?",
                    "水",
                    listOf("A) Fuego", "B) Agua", "C) Tierra", "D) Aire"),
                    "B) Agua"
                ),
                Question(
                    "2",
                    "¿Qué significa este kanji?",
                    "火",
                    listOf("A) Fuego", "B) Agua", "C) Tierra", "D) Aire"),
                    "A) Fuego"
                ),
                Question(
                    "3",
                    "¿Qué significa este hiragana?",
                    "あ",
                    listOf("A) A", "B) I", "C) U", "D) E"),
                    "A) A"
                ),
                Question(
                    "4",
                    "¿Qué significa este katakana?",
                    "ア",
                    listOf("A) A", "B) I", "C) U", "D) E"),
                    "A) A"
                ),
                Question(
                    "5",
                    "¿Cómo se dice 'gracias' en japonés?",
                    "?",
                    listOf("A) Konnichiwa", "B) Arigatou", "C) Sayonara", "D) Ohayou"),
                    "B) Arigatou"
                )
            )

            _state.update {
                it.copy(
                    functionName = functionName,
                    questions = questions,
                    isLoading = false
                )
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

    private fun finishLesson() {
        _state.update { it.copy(showResults = true) }
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