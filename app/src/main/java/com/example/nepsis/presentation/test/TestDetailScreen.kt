package com.example.nepsis.presentation.test

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestDetailScreen(
    testId: String,
    onNavigateBack: () -> Unit,
    onStartTest: (String) -> Unit
) {
    // Datos simulados según el ID seleccionado
    val title = if (testId == "vocacional_1") "Test Vocacional" else "Evaluación Nepsis"
    val description = "Este test te ayudará a descubrir tu perfil y obtener recomendaciones personalizadas basadas en tus respuestas. Asegúrate de contestar con honestidad."

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Test") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Text(
                        text = "⏱️ Duración estimada: 10 - 15 min",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Button(
                onClick = { onStartTest(testId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Comenzar Test", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
