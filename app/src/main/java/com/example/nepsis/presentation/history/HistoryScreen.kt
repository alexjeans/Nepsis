package com.example.nepsis.presentation.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nepsis.core.di.ServiceLocator
import com.example.nepsis.model.AreaVocacional
import com.example.nepsis.ui.components.InfoPill
import com.example.nepsis.ui.components.ModernCard
import com.example.nepsis.ui.components.SectionTitle
import com.example.nepsis.ui.components.UamBackground
import com.example.nepsis.ui.theme.UamAccent
import com.example.nepsis.ui.theme.UamPrimary
import com.example.nepsis.ui.theme.UamTextSecondary


@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = viewModel(factory = HistoryViewModelFactory(ServiceLocator.provideNepsisRepository(LocalContext.current)))
) {
    val state by viewModel.state.collectAsState()
    val historial = state.results

    UamBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 })
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SectionTitle(
                        titulo = "Historial",
                        subtitulo = "Tus resultados de pruebas guardados"
                    )

                    if (historial.isEmpty()) {
                        ModernCard {
                            InfoPill(text = "Sin resultados")

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Aún no has realizado ningún test.",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = UamPrimary
                            )

                            Text(
                                text = "Cuando completes un test vocacional, tus resultados aparecerán en esta pantalla.",
                                color = UamTextSecondary
                            )
                        }
                    } else {
                        historial.forEachIndexed { index, resultado ->
                            val area = AreaVocacional.entries.find { it.titulo == resultado.resultText }
                            val porcentaje = resultado.totalScore // Asumiendo que guardamos el % o puntaje directo

                            ModernCard {
                                InfoPill(text = "Resultado ${historial.size - index}")

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = area?.titulo ?: resultado.resultText,
                                    color = UamPrimary,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = area?.descripcion ?: "Resultado de tu evaluación vocacional.",
                                    color = UamTextSecondary
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Compatibilidad: $porcentaje%",
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                LinearProgressIndicator(
                                    progress = porcentaje / 100f,
                                    color = UamPrimary,
                                    trackColor = UamAccent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}