package com.example.nepsis.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nepsis.core.utils.Resource
import com.example.nepsis.core.utils.SessionManager
import com.example.nepsis.data.local.entity.ProfileEntity
import com.example.nepsis.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: ProfileEntity? = null,
    val error: String? = null
)

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadLocalProfile()
        syncWithSession()
    }

    private fun syncWithSession() {
        val userId = sessionManager.getUserId()
        val token = sessionManager.getToken()
        if (userId != null && token != null) {
            syncProfile(userId, token)
        }
    }

    private fun loadLocalProfile() {
        viewModelScope.launch {
            repository.getLocalProfile().collect { profileEntity ->
                _state.value = _state.value.copy(profile = profileEntity)
            }
        }
    }

    fun syncProfile(userId: String, token: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = repository.fetchAndSaveProfile(userId, token)) {
                is Resource.Success -> _state.value = _state.value.copy(isLoading = false, error = null)
                is Resource.Error -> _state.value = _state.value.copy(isLoading = false, error = result.message)
                is Resource.Loading -> {}
            }
        }
    }
}

class ProfileViewModelFactory(
    private val repository: ProfileRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}