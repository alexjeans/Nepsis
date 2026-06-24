package com.example.nepsis.presentation.test

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Option(val text: String, val score: Int)
data class Question(val id: Int, val text: String, val options: List<Option>)

class TestQuestionsViewModel : ViewModel() {

    // Datos simulados (Mock) para el MVP. Luego se pueden traer de Room/Supabase.
    private val _questions = MutableStateFlow(
        listOf(
            Question(1, "¿Qué prefieres hacer en tu tiempo libre?", listOf(
                Option("Resolver problemas de lógica o armar cosas", 3),
                Option("Ayudar a otros o escuchar sus problemas", 2),
                Option("Dibujar, leer o actividades creativas", 1)
            )),
            Question(2, "¿Cómo actúas bajo presión?", listOf(
                Option("Analizo la situación fríamente", 3),
                Option("Busco dialogar para encontrar una solución", 2),
                Option("Intento relajarme y buscar alternativas creativas", 1)
            )),
            Question(3, "¿Qué materia disfrutas más?", listOf(
                Option("Matemáticas / Programación", 3),
                Option("Psicología / Ciencias Sociales", 2),
                Option("Arte / Literatura", 1)
            ))
        )
    )
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _totalScore = MutableStateFlow(0)
    val totalScore: StateFlow<Int> = _totalScore.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    fun answerQuestion(score: Int) {
        _totalScore.value += score
        if (_currentIndex.value < _questions.value.size - 1) {
            _currentIndex.value += 1
        } else {
            _isFinished.value = true
        }
    }
}
