package com.example.nepsis.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nepsis.data.local.dao.NepsisDao
import com.example.nepsis.data.local.entity.TestResultEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class HistoryState(
    val isLoading: Boolean = false,
    val results: List<TestResultEntity> = emptyList(),
    val error: String? = null
)

class HistoryViewModel(
    private val dao: NepsisDao
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadFullHistory()
    }

    private fun loadFullHistory() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val profile = dao.getProfile().firstOrNull()
            
            if (profile != null) {
                dao.getTestResultsByUserId(profile.id)
                    .catch { e ->
                        _state.value = _state.value.copy(isLoading = false, error = e.message)
                    }
                    .collect { results ->
                        _state.value = _state.value.copy(isLoading = false, results = results)
                    }
            } else {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}

class HistoryViewModelFactory(
    private val dao: NepsisDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(dao) as T
    }
}