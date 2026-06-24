package com.example.nepsis.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyCheckInScreen(
    viewModel: HomeViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    var energy by remember { mutableFloatStateOf(3f) }
    var stress by remember { mutableFloatStateOf(3f) }
    var selectedEmotion by remember { mutableStateOf("") }
    val emotions = listOf("Feliz", "Tranquilo", "Ansioso", "Triste", "Enojado", "Agotado")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Check-In Diario") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "¿Cómo te sentís hoy?",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // Selector de Emoción (Chips)
            Text(text = "Estado emocional principal", style = MaterialTheme.typography.titleMedium)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(emotions) { emotion ->
                    FilterChip(
                        selected = selectedEmotion == emotion,
                        onClick = { selectedEmotion = emotion },
                        label = { Text(emotion) }
                    )
                }
            }

            // Slider de Energía
            Column {
                Text(text = "Nivel de energía: ${energy.toInt()}", style = MaterialTheme.typography.titleMedium)
                Slider(
                    value = energy,
                    onValueChange = { energy = it },
                    valueRange = 1f..5f,
                    steps = 3
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Muy baja", style = MaterialTheme.typography.bodySmall)
                    Text("Muy alta", style = MaterialTheme.typography.bodySmall)
                }
            }

            // Slider de Estrés
            Column {
                Text(text = "Nivel de estrés: ${stress.toInt()}", style = MaterialTheme.typography.titleMedium)
                Slider(
                    value = stress,
                    onValueChange = { stress = it },
                    valueRange = 1f..5f,
                    steps = 3,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.error,
                        activeTrackColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Relajado", style = MaterialTheme.typography.bodySmall)
                    Text("Al límite", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón de Guardar
            Button(
                onClick = {
                    viewModel.saveMood(
                        energy = energy.toInt(),
                        stress = stress.toInt(),
                        emotion = selectedEmotion.ifEmpty { "Neutral" }
                    )
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Guardar Registro")
                }
            }
        }
    }
}
