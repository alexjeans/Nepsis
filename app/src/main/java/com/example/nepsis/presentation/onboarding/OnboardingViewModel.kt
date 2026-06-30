package com.example.nepsis.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nepsis.core.utils.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    // Estados para guardar la información temporalmente
    var name = MutableStateFlow("")
    var age = MutableStateFlow("")
    var gender = MutableStateFlow("")
    var goal = MutableStateFlow("")

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    fun completeOnboarding() {
        viewModelScope.launch {
            // TODO: En el futuro guardaremos nombre, edad, género y objetivo en Room/Supabase
            userPreferences.saveOnboardingCompleted(true)
            _isCompleted.value = true
        }
    }
}