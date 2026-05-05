package com.example.nepsis.presentation.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nepsis.model.PerfilEstudiante
import com.example.nepsis.model.ResultadoVocacional
import com.example.nepsis.ui.components.InfoPill
import com.example.nepsis.ui.components.ModernCard
import com.example.nepsis.ui.components.SectionTitle
import com.example.nepsis.ui.components.StatCard
import com.example.nepsis.ui.components.UamBackground
import com.example.nepsis.ui.theme.UamAccent
import com.example.nepsis.ui.theme.UamBackground
import com.example.nepsis.ui.theme.UamPrimary
import com.example.nepsis.ui.theme.UamPrimaryDark
import com.example.nepsis.ui.theme.UamTextSecondary

@Composable
fun ProfileScreen(
    perfil: PerfilEstudiante?,
    historial: List<ResultadoVocacional>
) {
    val ultimaArea = historial.lastOrNull()?.areaPrincipal?.titulo ?: "Sin resultado"

    val iniciales = perfil?.nombre
        ?.trim()
        ?.split(" ")
        ?.filter { it.isNotBlank() }
        ?.mapNotNull { it.firstOrNull()?.uppercaseChar()?.toString() }
        ?.take(2)
        ?.joinToString("")
        ?: "U"

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
                        titulo = "Perfil",
                        subtitulo = "Información del estudiante"
                    )

                    ModernCard {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(UamAccent)
                                    .padding(28.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = iniciales,
                                    color = UamPrimaryDark,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = perfil?.nombre ?: "Estudiante no registrado",
                                color = UamPrimary,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Correo: ${perfil?.correo ?: "No registrado"}",
                                color = UamTextSecondary,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "Colegio: ${perfil?.colegio ?: "No registrado"}",
                                color = UamTextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            titulo = "Tests",
                            valor = historial.size.toString(),
                            emoji = "📘",
                            modifier = Modifier.weight(1f)
                        )

                        StatCard(
                            titulo = "Área",
                            valor = ultimaArea,
                            emoji = "🎯",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    ModernCard {
                        InfoPill(text = "Propósito")

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Ruta Ingeniería en Sistemas - Psicología",
                            style = MaterialTheme.typography.titleMedium,
                            color = UamPrimary,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Esta aplicación apoya la orientación profesional de estudiantes que egresan de secundaria y aspiran a ingresar a la universidad.",
                            color = UamTextSecondary
                        )
                    }

                    ModernCard {
                        InfoPill(text = "Nota importante")

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "La aplicación es educativa y orientativa. No realiza diagnósticos psicológicos ni reemplaza el acompañamiento profesional.",
                            color = UamTextSecondary
                        )
                    }
                }
            }
        }
    }
}