package com.example.aprendejapones.presentation.screens.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.utils.Achievement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AchievementsViewModel : ViewModel() {

    private val _state = MutableStateFlow(AchievementsState(isLoading = true))
    val state: StateFlow<AchievementsState> = _state

    init {
        loadAchievements()
    }

    private fun loadAchievements() {
        viewModelScope.launch {
            // Aquí deberías conectarlo con tu repositorio/datasource real.
            // Ejemplo ficticio de logros y carga “simulada”:
            val fakeAchievements = listOf(
                Achievement("1", "🎓", "Primer Logro", "Completa la primera lección", true),
                Achievement("2", "🌸", "Aprendiz", "Completa 5 lecciones", false),
                Achievement("3", "🔥", "Domina Kana", "Aprende todo Hiragana y Katakana", false),
                Achievement("4", "⭐", "Constante", "Estudia 7 días seguidos", true),
            )
            val unlocked = fakeAchievements.count { it.isUnlocked }
            val total = fakeAchievements.size
            _state.value = AchievementsState(
                isLoading = false,
                achievements = fakeAchievements,
                unlockedCount = unlocked,
                totalCount = total,
            )
        }
    }
}