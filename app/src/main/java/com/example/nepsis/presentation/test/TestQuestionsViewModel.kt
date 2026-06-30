package com.example.nepsis.presentation.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nepsis.core.utils.SessionManager
import com.example.nepsis.data.local.dao.NepsisDao
import com.example.nepsis.data.local.entity.TestResultEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class Option(val text: String, val score: Int)
data class Question(val id: Int, val text: String, val options: List<Option>)

class TestQuestionsViewModel(
    private val dao: NepsisDao,
    private val sessionManager: SessionManager, // Necesario para aislar los datos correctamente
    private val testId: String
) : ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    // Diccionario temporal: [Indice de pregunta] -> [Score de la opción seleccionada]
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

    var finalResultText = ""

    init {
        _questions.value = TestProvider.getQuestions(testId)
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
            checkPreviousResultAndFinish()
        }
    }

    fun onPreviousClicked() {
        if (_currentIndex.value > 0) {
            _currentIndex.value -= 1
            _showValidationError.value = false
        }
    }

    private fun checkPreviousResultAndFinish() {
        viewModelScope.launch {
            val userId = sessionManager.getUserId() ?: return@launch
            
            val previousResults = dao.getTestResultsByUserId(userId).firstOrNull() ?: emptyList()
            val hasPrevious = previousResults.any { it.testId == testId }

            if (hasPrevious) {
                _showReplaceDialog.value = true
            } else {
                saveAndFinish(replace = true)
            }
        }
    }

    fun dismissDialog() {
        _showReplaceDialog.value = false
    }

    fun saveAndFinish(replace: Boolean) {
        _showReplaceDialog.value = false
        
        // Sumar puntuación temporal
        val total = _selectedAnswers.value.values.sum()
        _totalScore.value = total
        finalResultText = TestProvider.getResult(testId, total)

        if (replace) {
            viewModelScope.launch {
                val userId = sessionManager.getUserId() ?: "unknown_user"
                val previousResults = dao.getTestResultsByUserId(userId).firstOrNull() ?: emptyList()
                val oldResult = previousResults.find { it.testId == testId }

                if (oldResult != null) {
                    // Actualiza el existente, evitando duplicados
                    val updatedResult = oldResult.copy(
                        totalScore = total,
                        resultText = finalResultText,
                        createdAt = System.currentTimeMillis()
                    )
                    dao.updateTestResult(updatedResult)
                } else {
                    // Inserta nuevo
                    val newResult = TestResultEntity(
                        userId = userId,
                        testId = testId,
                        totalScore = total,
                        resultText = finalResultText
                    )
                    dao.insertTestResult(newResult)
                }
                _isFinished.value = true
            }
        } else {
            // Si el usuario cancela, no guardamos pero finalizamos para ver el resultado de todas formas
            _isFinished.value = true
        }
    }
}

class TestQuestionsViewModelFactory(
    private val dao: NepsisDao,
    private val sessionManager: SessionManager,
    private val testId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TestQuestionsViewModel(dao, sessionManager, testId) as T
    }
}