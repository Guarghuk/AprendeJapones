package com.example.aprendejapones.presentation.screens.lesson

/**
 * Estado de LessonScreen
 */
// Cambios en LessonState: Hacer totalQuestions derivado (computed property) para consistencia.
data class LessonState(
    val functionName: String = "",
    val currentQuestion: Int = 1,
    val selectedAnswer: String? = null,
    val correctAnswers: Int = 0,
    val isAnswered: Boolean = false,
    val showResults: Boolean = false,
    val questions: List<Question> = emptyList(),
    val isLoading: Boolean = true
) {
    val totalQuestions: Int get() = questions.size
}
data class Question(
    val id: String,
    val text: String,
    val content: String,
    val options: List<String>,
    val correctAnswer: String
)

/**
 * Eventos de Lesson
 */
sealed class LessonEvent {
    data class LoadLesson(val functionName: String) : LessonEvent()
    data class SelectAnswer(val answer: String) : LessonEvent()
    object VerifyAnswer : LessonEvent()
    object NextQuestion : LessonEvent()
    object PreviousQuestion : LessonEvent()
    object FinishLesson : LessonEvent()
    object RestartLesson : LessonEvent()
}

/**
 * Efectos secundarios
 */
sealed class LessonEffect {
    object NavigateBack : LessonEffect()
    data class ShowToast(val message: String) : LessonEffect()
}