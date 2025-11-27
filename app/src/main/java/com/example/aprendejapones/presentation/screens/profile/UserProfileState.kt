package com.example.aprendejapones.presentation.screens.profile

import com.example.aprendejapones.domain.model.FirestoreUser

/**
 * State for viewing another user's profile
 */
data class UserProfileState(
    val user: FirestoreUser? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
