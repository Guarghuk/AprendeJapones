package com.example.aprendejapones.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.repository.AuthRepository
import com.example.aprendejapones.domain.repository.FirestoreUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreUserRepository: FirestoreUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<EditProfileEffect>()
    val effects: SharedFlow<EditProfileEffect> = _effects.asSharedFlow()

    init {
        loadCurrentProfile()
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.UsernameChanged -> updateUsername(event.username)
            is EditProfileEvent.BioChanged -> updateBio(event.bio)
            is EditProfileEvent.SelectPhoto -> selectPhoto()
            is EditProfileEvent.SaveProfile -> saveProfile()
            is EditProfileEvent.DismissError -> dismissError()
        }
    }

    private fun loadCurrentProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val userId = authRepository.getCurrentUserId()
                if (userId != null) {
                    val user = firestoreUserRepository.getUserProfile(userId)
                    if (user != null) {
                        _state.update {
                            it.copy(
                                userId = userId,
                                username = user.username,
                                bio = user.bio ?: "",
                                photoUrl = user.photoUrl,
                                originalUsername = user.username,
                                originalBio = user.bio ?: "",
                                isLoading = false
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "No se pudo cargar el perfil"
                            )
                        }
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "No has iniciado sesión"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error al cargar el perfil"
                    )
                }
            }
        }
    }

    private fun updateUsername(username: String) {
        _state.update { it.copy(username = username) }
    }

    private fun updateBio(bio: String) {
        _state.update { it.copy(bio = bio) }
    }

    private fun selectPhoto() {
        // TODO: Implement photo picker using Activity Result API
        // This requires the screen to handle the photo picker result and pass the Uri back to ViewModel
        // For now, notify user that this feature requires additional setup
        viewModelScope.launch {
            _effects.emit(EditProfileEffect.ShowToast("📷 Función en desarrollo - Próximamente"))
        }
    }

    private fun saveProfile() {
        if (_state.value.username.isBlank()) {
            _state.update { it.copy(error = "El nombre de usuario no puede estar vacío") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }

            try {
                val fields = mutableMapOf<String, Any>(
                    "username" to _state.value.username
                )
                
                if (_state.value.bio.isNotBlank()) {
                    fields["bio"] = _state.value.bio
                }

                val result = firestoreUserRepository.updateUserFields(
                    _state.value.userId,
                    fields
                )

                result.fold(
                    onSuccess = {
                        _state.update {
                            it.copy(
                                isSaving = false,
                                originalUsername = it.username,
                                originalBio = it.bio
                            )
                        }
                        _effects.emit(EditProfileEffect.SaveSuccess)
                    },
                    onFailure = { error ->
                        _state.update {
                            it.copy(
                                isSaving = false,
                                error = error.message ?: "Error al guardar"
                            )
                        }
                        _effects.emit(EditProfileEffect.ShowError(error.message ?: "Error al guardar"))
                    }
                )
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        error = e.message ?: "Error al guardar"
                    )
                }
                _effects.emit(EditProfileEffect.ShowError(e.message ?: "Error al guardar"))
            }
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}
