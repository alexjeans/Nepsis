package com.example.nepsis.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nepsis.core.utils.UserPreferences
import com.example.nepsis.data.local.dao.NepsisDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val userPreferences: UserPreferences,
    private val dao: NepsisDao // Inyectamos el DAO para guardar el perfil local
) : ViewModel() {

    var name = MutableStateFlow("")
    var age = MutableStateFlow("")
    var gender = MutableStateFlow("")
    var goal = MutableStateFlow("")

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    fun completeOnboarding() {
        viewModelScope.launch {
            // 1. Obtenemos el perfil actual (creado en el Login)
            val currentProfile = dao.getProfile().firstOrNull()
            
            // 2. Lo actualizamos con los datos del Onboarding
            if (currentProfile != null) {
                val updatedProfile = currentProfile.copy(
                    fullName = name.value.ifBlank { currentProfile.fullName },
                    age = age.value.toIntOrNull() ?: 0,
                    gender = gender.value,
                    goal = goal.value
                )
                dao.insertProfile(updatedProfile) // REPLACES el perfil viejo con el actualizado
            }

            // 3. Marcamos el Onboarding como completado en DataStore
            userPreferences.saveOnboardingCompleted(true)
            _isCompleted.value = true
        }
    }
}