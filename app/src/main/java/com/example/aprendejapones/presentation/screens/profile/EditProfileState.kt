package com.example.aprendejapones.presentation.screens.profile

/**
 * State for Edit Profile screen
 */
data class EditProfileState(
    val userId: String = "",
    val username: String = "",
    val bio: String = "",
    val photoUrl: String? = null,
    val originalUsername: String = "",
    val originalBio: String = "",
    val isSaving: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val hasChanges: Boolean
        get() = username != originalUsername || bio != originalBio
}

/**
 * Events for Edit Profile screen
 */
sealed class EditProfileEvent {
    data class UsernameChanged(val username: String) : EditProfileEvent()
    data class BioChanged(val bio: String) : EditProfileEvent()
    object SelectPhoto : EditProfileEvent()
    object SaveProfile : EditProfileEvent()
    object DismissError : EditProfileEvent()
}

/**
 * One-time effects for Edit Profile screen
 */
sealed class EditProfileEffect {
    object SaveSuccess : EditProfileEffect()
    data class ShowError(val message: String) : EditProfileEffect()
    data class ShowToast(val message: String) : EditProfileEffect()
}
