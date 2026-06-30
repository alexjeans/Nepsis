package com.example.nepsis.presentation.test

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestQuestionsScreen(
    viewModel: TestQuestionsViewModel,
    onNavigateBack: () -> Unit,
    onTestFinished: (Int, String) -> Unit
) {
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val selectedAnswers by viewModel.selectedAnswers.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()
    val showValidationError by viewModel.showValidationError.collectAsState()
    val showReplaceDialog by viewModel.showReplaceDialog.collectAsState()

    LaunchedEffect(isFinished) {
        if (isFinished) {
            onTestFinished(viewModel.totalScore.value, viewModel.finalResultText)
        }
    }

    if (questions.isEmpty()) return

    val currentQuestion = questions[currentIndex]
    val currentSelectedScore = selectedAnswers[currentIndex]
    val isLastQuestion = currentIndex == questions.size - 1

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pregunta ${currentIndex + 1} de ${questions.size}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                LinearProgressIndicator(
                    progress = { (currentIndex + 1) / questions.size.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(
                    text = currentQuestion.text,
                    style = MaterialTheme.typography.titleLarge
                )

                if (showValidationError) {
                    Text(
                        text = "Por favor, selecciona una opción para avanzar.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                currentQuestion.options.forEach { option ->
                    val isSelected = currentSelectedScore == option.score
                    Card(
                        onClick = { viewModel.selectOption(option.score) },
                        modifier = Modifier.fillMaxWidth(),
                        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            // Se eliminó por completo la puntuación de la vista
                            Text(text = option.text, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentIndex > 0) {
                    OutlinedButton(onClick = { viewModel.onPreviousClicked() }) {
                        Text("Anterior")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Button(onClick = { viewModel.onNextClicked() }) {
                    Text(if (isLastQuestion) "Finalizar" else "Siguiente")
                }
            }
        }
    }

    if (showReplaceDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDialog() },
            title = { Text("Módulo ya completado") },
            text = { Text("Tienes un resultado guardado para este módulo. ¿Deseas reemplazarlo por el nuevo resultado?") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.saveAndFinish(replace = true) }
                ) {
                    Text("Reemplazar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.saveAndFinish(replace = false) }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}