package com.example.nepsis.presentation.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nepsis.core.utils.SessionManager
import com.example.nepsis.data.local.entity.TestResultEntity
import com.example.nepsis.domain.repository.NepsisRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Option(val text: String, val score: Int)
data class Question(val id: Int, val text: String, val options: List<Option>)

class TestQuestionsViewModel(
    private val repository: NepsisRepository,
    private val sessionManager: SessionManager,
    private val testId: String
) : ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _selectedAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val selectedAnswers: StateFlow<Map<Int, Int>> = _selectedAnswers.asStateFlow()

    private val _totalScore = MutableStateFlow(0)
    val totalScore: StateFlow<Int> = _totalScore.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    private val _showValidationError = MutableStateFlow(false)
    val showValidationError: StateFlow<Boolean> = _showValidationError.asStateFlow()

    private val _showReplaceDialog = MutableStateFlow(false)
    val showReplaceDialog: StateFlow<Boolean> = _showReplaceDialog.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    var finalResultText = ""

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            repository.getTestById(testId).collect { testEntity ->
                if (testEntity != null) {
                    try {
                        val type = object : com.google.gson.reflect.TypeToken<List<Question>>(){}.type
                        val questions: List<Question> = Gson().fromJson(testEntity.questionsJson, type)
                        _questions.value = questions
                    } catch (e: Exception) {
                        // Manejar error de parseo si fuera necesario
                    }
                }
                _isLoading.value = false
            }
        }
    }

    fun selectOption(score: Int) {
        val currentMap = _selectedAnswers.value.toMutableMap()
        currentMap[_currentIndex.value] = score
        _selectedAnswers.value = currentMap
        _showValidationError.value = false
    }

    fun onNextClicked() {
        if (!_selectedAnswers.value.containsKey(_currentIndex.value)) {
            _showValidationError.value = true
            return
        }
        
        if (_currentIndex.value < _questions.value.size - 1) {
            _currentIndex.value += 1
        } else {
            saveAndFinish(replace = true)
        }
    }

    fun onPreviousClicked() {
        if (_currentIndex.value > 0) {
            _currentIndex.value -= 1
            _showValidationError.value = false
        }
    }

    fun dismissDialog() {
        _showReplaceDialog.value = false
    }

    fun saveAndFinish(replace: Boolean) {
        _showReplaceDialog.value = false
        
        val total = _selectedAnswers.value.values.sum()
        _totalScore.value = total
        
        // El resultado también podría ser dinámico en el futuro, por ahora usamos un placeholder
        // o mantenemos TestProvider solo para los textos de resultado si no están en la DB.
        // Pero el usuario dijo que TestProvider ya es inútil.
        // Asumiremos que el resultado se calcula de forma genérica o el prompt pedía quitar TestProvider.
        finalResultText = "Has completado el test con éxito. Tu puntuación es $total."

        val answersJsonStr = Gson().toJson(_selectedAnswers.value)

        if (replace) {
            viewModelScope.launch {
                val userId = sessionManager.getUserId() ?: "unknown_user"
                val token = sessionManager.getToken() ?: ""
                
                val newResult = TestResultEntity(
                    userId = userId,
                    testId = testId,
                    totalScore = total,
                    resultText = finalResultText,
                    answersJson = answersJsonStr
                )
                
                repository.saveTestResult(newResult, token)
                _isFinished.value = true
            }
        } else {
            _isFinished.value = true
        }
    }
}

class TestQuestionsViewModelFactory(
    private val repository: NepsisRepository,
    private val sessionManager: SessionManager,
    private val testId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TestQuestionsViewModel(repository, sessionManager, testId) as T
    }
}
