package com.example.nepsis.presentation.nepsia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nepsis.BuildConfig
import com.example.nepsis.core.utils.SessionManager
import com.example.nepsis.data.local.dao.NepsisDao
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isFromUser: Boolean,
    val suggestions: List<String> = emptyList()
)

data class NepsiaState(
    val messages: List<ChatMessage> = emptyList(),
    val isTyping: Boolean = false
)

class NepsiaViewModel(
    private val dao: NepsisDao,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(NepsiaState())
    val state: StateFlow<NepsiaState> = _state.asStateFlow()

    private var chatSession: Chat? = null
    private val GEMINI_API_KEY = BuildConfig.GEMINI_API_KEY

    fun initializeChat() {
        viewModelScope.launch {
            try {
                val userId = sessionManager.getUserId() ?: return@launch

                val profile = dao.getProfileByUserId(userId).firstOrNull()
                val history = dao.getTestResultsByUserId(userId).firstOrNull() ?: emptyList()
                val moods = dao.getDailyMoodsByUserId(userId).firstOrNull() ?: emptyList()

                val userName = profile?.fullName?.split(" ")?.firstOrNull() ?: "amigo"
                val today = LocalDate.now().toString()
                val hasCheckedInToday = moods.any { it.date == today }
                val completedTests = history.map { it.testId }.joinToString(", ")

                val systemInstruction = """
                    Eres Nepsia, un asistente amigable de bienestar y motivación exclusivo de la app Nepsis.
                    No eres una IA genérica. Eres un coach.
                    El usuario con el que hablas se llama $userName.
                    Contexto actual de $userName:
                    - ¿Hizo su check-in emocional hoy?: ${if (hasCheckedInToday) "Sí" else "No"}
                    - Tests completados hasta ahora: $completedTests
                    
                    Reglas obligatorias:
                    - Responde de forma cercana, concisa y simple.
                    - Motiva al usuario a continuar usando la app.
                    - Si no ha hecho su check-in o le faltan tests, sugiérelo sutilmente.
                """.trimIndent()

                // CORRECCIÓN: Modelo válido (gemini-1.5-flash) y protegido
                val generativeModel = GenerativeModel(
                    modelName = "gemini-2.5-flash-lite",
                    apiKey = GEMINI_API_KEY,
                    systemInstruction = content { text(systemInstruction) }
                )

                chatSession = generativeModel.startChat()

                val dynamicSuggestions = mutableListOf<String>()
                if (!hasCheckedInToday) dynamicSuggestions.add("Registrar mi día")
                if (history.none { it.testId == "personalidad" }) dynamicSuggestions.add("Hacer test de personalidad")
                if (history.none { it.testId == "vocacional" }) dynamicSuggestions.add("Completar test vocacional")
                if (history.isNotEmpty()) dynamicSuggestions.add("Revisar historial")

                val welcomeMessage = ChatMessage(
                    text = "Hola, soy Nepsia 👋\n¿Cómo estás, $userName?\n¿En qué puedo ayudarte hoy?",
                    isFromUser = false,
                    suggestions = dynamicSuggestions
                )

                _state.value = NepsiaState(messages = listOf(welcomeMessage))

            } catch (e: Exception) {
                // Evitamos el crash fatal si el SDK o la API Key fallan
                val errorMsg = ChatMessage(text = "Nepsia está desconectada. Revisa tu internet o configuración.", isFromUser = false)
                _state.value = NepsiaState(messages = listOf(errorMsg))
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val userMsg = ChatMessage(text = text, isFromUser = true)
        _state.value = _state.value.copy(
            messages = _state.value.messages + userMsg,
            isTyping = true
        )

        viewModelScope.launch {
            try {
                val response = chatSession?.sendMessage(text)
                val aiResponseText = response?.text ?: "Nepsia está un poco cansada, intenta de nuevo en un momento."

                _state.value = _state.value.copy(
                    messages = _state.value.messages + ChatMessage(text = aiResponseText, isFromUser = false),
                    isTyping = false
                )
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("503") == true -> "Nepsia está recibiendo muchas consultas ahora mismo. Dame un segundo."
                    else -> "Error de red. Intenta de nuevo."
                }

                _state.value = _state.value.copy(
                    messages = _state.value.messages + ChatMessage(text = errorMessage, isFromUser = false),
                    isTyping = false
                )
            }
        }
    }

    fun handleSuggestionClick(suggestion: String, onNavigate: (String) -> Unit) {
        sendMessage(suggestion)
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            when (suggestion) {
                "Hacer test de personalidad" -> onNavigate("test_detail/personalidad")
                "Completar test vocacional" -> onNavigate("test_detail/vocacional")
                "Registrar mi día" -> onNavigate("daily_checkin")
                "Revisar historial" -> onNavigate("history")
            }
        }
    }
}

class NepsiaViewModelFactory(
    private val dao: NepsisDao,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NepsiaViewModel(dao, sessionManager) as T
    }
}