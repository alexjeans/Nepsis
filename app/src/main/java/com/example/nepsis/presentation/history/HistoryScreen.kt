package com.example.nepsis.presentation.history

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nepsis.presentation.test.TestProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial Completo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.results.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Aún no has realizado ningún test", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.results) { result ->
                        var isExpanded by remember { mutableStateOf(false) }
                        val testInfo = TestProvider.getTestInfo(result.testId)
                        val questionsList = TestProvider.getQuestions(result.testId)
                        val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(result.createdAt))

                        // Parsear respuestas JSON
                        val type = object : TypeToken<Map<Int, Int>>() {}.type
                        val answersMap: Map<Int, Int> = try {
                            Gson().fromJson(result.answersJson, type) ?: emptyMap()
                        } catch (e: Exception) { emptyMap() }

                        Card(
                            modifier = Modifier.fillMaxWidth().clickable { isExpanded = !isExpanded },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = testInfo.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Resultado: ${result.resultText}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Text(text = "Fecha: $dateStr", style = MaterialTheme.typography.labelSmall)

                                AnimatedVisibility(visible = isExpanded) {
                                    Column(modifier = Modifier.padding(top = 16.dp)) {
                                        HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
                                        Text("Respuestas seleccionadas:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                                        Spacer(modifier = Modifier.height(8.dp))

                                        questionsList.forEachIndexed { index, question ->
                                            val selectedScore = answersMap[index]
                                            // Buscar el texto de la opción que coincide con la opción que eligió
                                            val selectedOptionText = question.options.find { it.score == selectedScore }?.text ?: "No respondida"

                                            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                                                Text(text = "P: ${question.text}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                                                Text(text = "R: $selectedOptionText", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}