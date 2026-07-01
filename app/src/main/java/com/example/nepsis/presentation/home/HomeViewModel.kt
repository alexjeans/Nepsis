package com.example.nepsis.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nepsis.core.utils.Resource
import com.example.nepsis.core.utils.SessionManager
import com.example.nepsis.data.local.entity.DailyMoodEntity
import com.example.nepsis.domain.repository.NepsisRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HomeState(
    val isLoading: Boolean = false,
    val moods: List<DailyMoodEntity> = emptyList(),
    val error: String? = null
)

class HomeViewModel(
    private val repository: NepsisRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadLocalMoods()
        syncWithCloud()
    }

    private fun syncWithCloud() {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            val token = sessionManager.getToken()
            
            if (userId != null && token != null) {
                repository.syncData(userId, token)
            }
        }
    }

    private fun loadLocalMoods() {
        val userId = sessionManager.getUserId()
        
        if (userId == null) {
            _state.value = _state.value.copy(error = "No hay sesión activa")
            return
        }

        viewModelScope.launch {
            repository.getLocalMoods(userId)
                .catch { e -> _state.value = _state.value.copy(error = e.message) }
                .collect { moods ->
                    _state.value = _state.value.copy(moods = moods)
                }
        }
    }

    fun saveMood(energy: Int, stress: Int, emotion: String) {
        val userId = sessionManager.getUserId() ?: return
        val token = sessionManager.getToken() ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            val newMood = DailyMoodEntity(
                userId = userId,
                energyLevel = energy,
                stressLevel = stress,
                emotionalState = emotion,
                date = LocalDate.now().toString()
            )
            
            when (val result = repository.saveDailyMood(newMood, token)) {
                is Resource.Success -> _state.value = _state.value.copy(isLoading = false, error = null)
                is Resource.Error -> _state.value = _state.value.copy(isLoading = false, error = result.message)
                is Resource.Loading -> { }
            }
        }
    }
}

class HomeViewModelFactory(
    private val repository: NepsisRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}
