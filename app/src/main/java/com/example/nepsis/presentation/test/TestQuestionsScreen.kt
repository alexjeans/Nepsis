package com.example.nepsis.presentation.test

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestQuestionsScreen(
    testId: String,
    viewModel: TestQuestionsViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onTestFinished: (Int, String) -> Unit
) {
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()
    val totalScore by viewModel.totalScore.collectAsState()

    var selectedOption by remember { mutableStateOf<Option?>(null) }

    // Navegar al terminar
    LaunchedEffect(isFinished) {
        if (isFinished) {
            val result = when {
                totalScore >= 8 -> "Perfil_Ingeniería_y_Tecnología"
                totalScore in 5..7 -> "Perfil_Ciencias_Sociales_y_Salud"
                else -> "Perfil_Arte_y_Humanidades"
            }
            onTestFinished(totalScore, result)
        }
    }

    if (questions.isNotEmpty() && !isFinished) {
        val currentQuestion = questions[currentIndex]

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Pregunta ${currentIndex + 1} de ${questions.size}") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.Close, contentDescription = "Cancelar Test")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
            ) {
                LinearProgressIndicator(
                    progress = { (currentIndex + 1) / questions.size.toFloat() },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = currentQuestion.text,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))

                currentQuestion.options.forEach { option ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { selectedOption = option },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedOption == option) MaterialTheme.colorScheme.primaryContainer 
                                             else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedOption == option,
                                onClick = { selectedOption = option }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = option.text, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        selectedOption?.let {
                            viewModel.answerQuestion(it.score)
                            selectedOption = null // Resetear para la siguiente pregunta
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = selectedOption != null
                ) {
                    Text(if (currentIndex == questions.size - 1) "Finalizar" else "Siguiente")
                }
            }
        }
    }
}
