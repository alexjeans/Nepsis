package com.example.nepsis.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nepsis.data.local.entity.TestResultEntity
import com.example.nepsis.domain.repository.NepsisRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class HistoryState(
    val results: List<TestResultEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HistoryViewModel(
    private val repository: NepsisRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadResults()
    }

    private fun loadResults() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.getLocalTestResults()
                .catch { e -> 
                    _state.value = _state.value.copy(isLoading = false, error = e.message) 
                }
                .collect { results ->
                    _state.value = _state.value.copy(isLoading = false, results = results, error = null)
                }
        }
    }
}

class HistoryViewModelFactory(private val repository: NepsisRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
