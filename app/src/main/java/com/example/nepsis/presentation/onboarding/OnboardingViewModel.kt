package com.example.nepsis.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nepsis.core.utils.Resource
import com.example.nepsis.core.utils.SessionManager
import com.example.nepsis.core.utils.UserPreferences
import com.example.nepsis.data.local.dao.NepsisDao
import com.example.nepsis.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. Añadimos estados para darle feedback a la UI
sealed class OnboardingState {
    data object Idle : OnboardingState()
    data object Loading : OnboardingState()
    data object Success : OnboardingState()
    data class Error(val message: String) : OnboardingState()
}

class OnboardingViewModel(
    private val userPreferences: UserPreferences,
    private val dao: NepsisDao,
    private val profileRepository: ProfileRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var name = MutableStateFlow("")
    var age = MutableStateFlow("")
    var gender = MutableStateFlow("")
    var goal = MutableStateFlow("")

    // 2. Reemplazamos isCompleted por la máquina de estados
    private val _uiState = MutableStateFlow<OnboardingState>(OnboardingState.Idle)
    val uiState: StateFlow<OnboardingState> = _uiState.asStateFlow()

    fun completeOnboarding() {
        viewModelScope.launch {
            _uiState.value = OnboardingState.Loading

            val userId = sessionManager.getUserId()
            val token = sessionManager.getToken()
            
            if (userId == null || token == null) {
                _uiState.value = OnboardingState.Error("Sesión inválida. Por favor, reinicia la aplicación.")
                return@launch
            }

            val parsedAge = age.value.toIntOrNull() ?: 0

            // 1. Mandar a Supabase
            val result = profileRepository.updateRemoteProfile(
                userId = userId,
                token = token,
                age = parsedAge,
                gender = gender.value,
                goal = goal.value
            )

            when (result) {
                is Resource.Success -> {
                    // 2. Sincronización Segura: Evitamos leer con Flow/firstOrNull que causa deadlock.
                    // Ya actualizamos el backend, simplemente forzamos la descarga del perfil fresco 
                    // a la caché local de Room para sincronizarlo inmediatamente.
                    val syncResult = profileRepository.fetchAndSaveProfile(userId, token)
                    
                    if (syncResult is Resource.Success) {
                        // 3. Marcar el Onboarding como completado en DataStore
                        userPreferences.saveOnboardingCompleted(true)
                        _uiState.value = OnboardingState.Success
                    } else if (syncResult is Resource.Error) {
                        _uiState.value = OnboardingState.Error(syncResult.message)
                    } else {
                        _uiState.value = OnboardingState.Error("Fallo al sincronizar base de datos local.")
                    }
                }
                is Resource.Error -> {
                    // Smart cast a Resource.Error permite acceder a .message
                    _uiState.value = OnboardingState.Error(result.message)
                }
                is Resource.Loading -> {
                    // Ya estamos en estado Loading
                }
            }
        }
    }
}
