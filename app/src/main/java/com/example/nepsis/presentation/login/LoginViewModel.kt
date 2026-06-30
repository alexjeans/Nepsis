package com.example.nepsis.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nepsis.core.utils.Resource
import com.example.nepsis.core.utils.SessionManager
import com.example.nepsis.core.utils.UserPreferences
import com.example.nepsis.data.local.dao.NepsisDao
import com.example.nepsis.data.remote.dto.SupabaseAuthResponse
import com.example.nepsis.domain.repository.AuthRepository
import com.example.nepsis.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

enum class LoginNavigationAction {
    NONE, GO_TO_HOME, GO_TO_ONBOARDING
}

data class LoginState(
    val isLoading: Boolean = false,
    val authData: SupabaseAuthResponse? = null,
    val error: String? = null,
    val navigationAction: LoginNavigationAction = LoginNavigationAction.NONE
)

class LoginViewModel(
    private val repository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val sessionManager: SessionManager,
    private val userPreferences: UserPreferences,
    private val dao: NepsisDao
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun loginWithSupabase(idToken: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = repository.loginWithGoogle(idToken)) {
                is Resource.Success -> {
                    val userId = result.data.user.id
                    val token = result.data.accessToken
                    
                    sessionManager.saveSession(token, userId)
                    
                    // Descarga el perfil que creó el Trigger en Supabase
                    profileRepository.fetchAndSaveProfile(userId, token)

                    val localProfile = dao.getProfileByUserId(userId).firstOrNull()
                    val isComplete = (localProfile?.age ?: 0) > 0 || !localProfile?.goal.isNullOrBlank()
                    
                    userPreferences.saveOnboardingCompleted(isComplete)

                    val nextAction = if (isComplete) LoginNavigationAction.GO_TO_HOME else LoginNavigationAction.GO_TO_ONBOARDING

                    _state.value = _state.value.copy(
                        isLoading = false,
                        authData = result.data,
                        navigationAction = nextAction,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(isLoading = false, error = result.message)
                }
                is Resource.Loading -> {}
            }
        }
    }
}

class LoginViewModelFactory(
    private val repository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val sessionManager: SessionManager,
    private val userPreferences: UserPreferences,
    private val dao: NepsisDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(repository, profileRepository, sessionManager, userPreferences, dao) as T
    }
}
