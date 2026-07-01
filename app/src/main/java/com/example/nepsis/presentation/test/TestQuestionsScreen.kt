package com.example.nepsis.presentation.test

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    val uiState by viewModel.uiState.collectAsState()
    val answers by viewModel.answers.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(if (uiState is TestState.Success) (uiState as TestState.Success).title else "Cargando Test...") 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (val state = uiState) {
                is TestState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is TestState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
                is TestState.Success -> {
                    val allAnswered = state.questions.size == answers.size

                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.questions) { question ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "${question.id}. ${question.question}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        
                                        question.options.forEach { option ->
                                            val isSelected = answers[question.id] == option.category
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { viewModel.selectOption(question.id, option.category) }
                                                    .padding(vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(
                                                    selected = isSelected,
                                                    onClick = { viewModel.selectOption(question.id, option.category) }
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(text = option.text)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Button(
                            enabled = allAnswered,
                            onClick = { viewModel.finishTest(onTestFinished) },
                            modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp)
                        ) {
                            Text("Finalizar Test")
                        }
                    }
                }
            }
        }
    }
}