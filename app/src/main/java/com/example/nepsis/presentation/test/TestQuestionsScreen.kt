package com.example.nepsis.presentation.test

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    val isFinished by viewModel.isFinished.collectAsState()

    // Cuando el ViewModel marque que terminó (y guardó en DB), navegamos al resultado
    LaunchedEffect(isFinished) {
        if (isFinished) {
            onTestFinished(viewModel.totalScore.value, viewModel.finalResultText)
        }
    }

    // Prevención de crasheos si la lista está vacía
    if (questions.isEmpty()) return

    val currentQuestion = questions[currentIndex]

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LinearProgressIndicator(
                progress = { (currentIndex + 1) / questions.size.toFloat() },
                modifier = Modifier.fillMaxWidth()
            )
            
            Text(
                text = currentQuestion.text,
                style = MaterialTheme.typography.titleLarge
            )

            currentQuestion.options.forEach { option ->
                Button(
                    onClick = { viewModel.answerQuestion(option.score) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(option.text)
                }
            }
        }
    }
}