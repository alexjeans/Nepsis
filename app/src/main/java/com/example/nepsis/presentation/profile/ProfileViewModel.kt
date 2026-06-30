package com.example.nepsis.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nepsis.core.utils.SessionManager
import com.example.nepsis.data.local.dao.NepsisDao
import com.example.nepsis.data.local.entity.ProfileEntity
import com.example.nepsis.data.local.entity.TestResultEntity
import com.example.nepsis.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: ProfileEntity? = null,
    val recentResults: List<TestResultEntity> = emptyList(),
    val error: String? = null
)

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val sessionManager: SessionManager,
    private val dao: NepsisDao
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadProfileAndHistory()
    }

    private fun loadProfileAndHistory() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            repository.getLocalProfile()
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
                .collect { profile ->
                    _state.value = _state.value.copy(profile = profile)
                    
                    if (profile != null) {
                        // Escuchar los resultados de test exclusivos de este usuario en tiempo real
                        dao.getTestResultsByUserId(profile.id)
                            .catch { e -> _state.value = _state.value.copy(error = e.message) }
                            .collect { results ->
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    recentResults = results
                                )
                            }
                    } else {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
        }
    }
}

class ProfileViewModelFactory(
    private val repository: ProfileRepository,
    private val sessionManager: SessionManager,
    private val dao: NepsisDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(repository, sessionManager, dao) as T
    }
}