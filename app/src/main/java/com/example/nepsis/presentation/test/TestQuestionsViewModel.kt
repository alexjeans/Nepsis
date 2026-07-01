package com.example.nepsis.presentation.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nepsis.core.utils.SessionManager
import com.example.nepsis.data.local.entity.TestResultEntity
import com.example.nepsis.domain.repository.NepsisRepository
import com.example.nepsis.model.QuestionModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

sealed class TestState {
    object Loading : TestState()
    data class Success(val title: String, val questions: List<QuestionModel>) : TestState()
    data class Error(val message: String) : TestState()
}

class TestQuestionsViewModel(
    private val repository: NepsisRepository,
    private val sessionManager: SessionManager,
    private val testId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<TestState>(TestState.Loading)
    val uiState: StateFlow<TestState> = _uiState.asStateFlow()

    // Mapa para guardar la categoría seleccionada por cada ID de pregunta (ej. {1: "Introvertido", 2: "Extrovertido"})
    private val _answers = MutableStateFlow<Map<Int, String>>(emptyMap())
    val answers: StateFlow<Map<Int, String>> = _answers.asStateFlow()

    private val gson = Gson()

    init {
        loadTest()
    }

    private fun loadTest() {
        viewModelScope.launch {
            repository.getTestById(testId).collect { testEntity ->
                if (testEntity != null) {
                    try {
                        // Magia: Parsear el String JSON a una Lista de QuestionModel
                        val type = object : TypeToken<List<QuestionModel>>() {}.type
                        val parsedQuestions: List<QuestionModel> = gson.fromJson(testEntity.questionsJson, type)
                        
                        _uiState.value = TestState.Success(
                            title = testEntity.title,
                            questions = parsedQuestions
                        )
                    } catch (e: Exception) {
                        _uiState.value = TestState.Error("Error al decodificar el test.")
                    }
                } else {
                    _uiState.value = TestState.Error("El test no se encuentra en la base de datos local.")
                }
            }
        }
    }

    fun selectOption(questionId: Int, category: String) {
        val currentAnswers = _answers.value.toMutableMap()
        currentAnswers[questionId] = category
        _answers.value = currentAnswers
    }

    fun finishTest(onResultReady: (Int, String) -> Unit) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            val token = sessionManager.getToken()

            if (userId == null || token == null) return@launch

            // 1. Evaluar resultado: Buscar la categoría que más se repite
            val categoryCounts = _answers.value.values.groupingBy { it }.eachCount()
            val topCategory = categoryCounts.maxByOrNull { it.value }?.key ?: "Indefinido"
            val resultText = "Tu resultado dominante es: $topCategory"

            // 2. Convertir las respuestas a JSON para guardarlas
            val answersJsonString = gson.toJson(_answers.value)

            // 3. Crear la entidad para Room
            val resultEntity = TestResultEntity(
                id = UUID.randomUUID().toString(), // Dependiendo de tu Room, si genera auto, puedes omitir
                userId = userId,
                testId = testId,
                totalScore = 0, // No usamos puntajes puros
                resultText = resultText,
                answersJson = answersJsonString,
                isSynced = false
            )

            // 4. Guardar y encolar a Supabase
            repository.saveTestResult(resultEntity, token)

            // 5. Navegar a la pantalla de resultados
            onResultReady(0, resultText)
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
        if (modelClass.isAssignableFrom(TestQuestionsViewModel::class.java)) {
            return TestQuestionsViewModel(repository, sessionManager, testId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}