package com.example.nepsis.presentation.test

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Modelo temporal para la interfaz
data class TestItem(val id: String, val title: String, val description: String, val duration: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(
    onNavigateToDetail: (String) -> Unit
) {
    val tests = listOf(
        TestItem("vocacional_1", "Test Vocacional", "Descubre tu perfil profesional y áreas de interés académico.", "10 min"),
        TestItem("personalidad_1", "Test de Personalidad", "Conoce tus rasgos predominantes y estilo de trabajo.", "15 min"),
        TestItem("estres_1", "Nivel de Estrés", "Evalúa tu carga actual y obtén recomendaciones de bienestar.", "5 min")
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Biblioteca de Tests", fontWeight = FontWeight.Bold) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tests) { test ->
                Card(
                    onClick = { onNavigateToDetail(test.id) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = test.title, 
                            style = MaterialTheme.typography.titleMedium, 
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = test.description, 
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "⏱️ ${test.duration}", 
                            style = MaterialTheme.typography.labelMedium, 
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
