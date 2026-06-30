package com.example.nepsis.presentation.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
    private val testId: String
) : ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _totalScore = MutableStateFlow(0)
    val totalScore: StateFlow<Int> = _totalScore.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    var finalResultText = ""

    init {
        _questions.value = TestProvider.getQuestions(testId)
    }

    fun answerQuestion(score: Int) {
        _totalScore.value += score
        if (_currentIndex.value < _questions.value.size - 1) {
            _currentIndex.value += 1
        } else {
            finishTest()
        }
    }

    private fun finishTest() {
        finalResultText = TestProvider.getResult(testId, _totalScore.value)
        
        viewModelScope.launch {
            // Obtenemos el usuario actual de Room para asociar el historial
            val currentProfile = dao.getProfile().firstOrNull()
            val userId = currentProfile?.id ?: "unknown_user"
            
            val result = TestResultEntity(
                userId = userId,
                testId = testId,
                totalScore = _totalScore.value,
                resultText = finalResultText
            )
            dao.insertTestResult(result) // Guarda el resultado real en la BD local
            _isFinished.value = true
        }
    }
}

// Factory para poder inyectarle el DAO y el testId
class TestQuestionsViewModelFactory(
    private val dao: NepsisDao,
    private val testId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TestQuestionsViewModel(dao, testId) as T
    }
}