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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

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

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    fun completeOnboarding() {
        viewModelScope.launch {
            val userId = sessionManager.getUserId() ?: return@launch
            val token = sessionManager.getToken() ?: return@launch
            val parsedAge = age.value.toIntOrNull() ?: 0

            // 1. Mandar a Supabase
            val result = profileRepository.updateRemoteProfile(
                userId = userId,
                token = token,
                age = parsedAge,
                gender = gender.value,
                goal = goal.value
            )

            if (result is Resource.Success) {
                // 2. Si hay éxito, actualizar Room
                val currentProfile = dao.getProfileByUserId(userId).firstOrNull()
                if (currentProfile != null) {
                    val updatedProfile = currentProfile.copy(
                        fullName = name.value.ifBlank { currentProfile.fullName },
                        age = parsedAge,
                        gender = gender.value,
                        goal = goal.value
                    )
                    dao.insertProfile(updatedProfile) 
                }
                
                // 3. Marcar el Onboarding como completado en DataStore
                userPreferences.saveOnboardingCompleted(true)
                _isCompleted.value = true
            }
        }
    }
}
