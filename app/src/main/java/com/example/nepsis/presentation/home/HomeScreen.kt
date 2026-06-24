package com.example.nepsis.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nepsis.core.di.ServiceLocator
import com.example.nepsis.data.VocacionalRepository
import com.example.nepsis.presentation.profile.ProfileViewModel
import com.example.nepsis.presentation.profile.ProfileViewModelFactory
import com.example.nepsis.ui.components.GradientButton
import com.example.nepsis.ui.components.InfoPill
import com.example.nepsis.ui.components.ModernCard
import com.example.nepsis.ui.components.SectionTitle
import com.example.nepsis.ui.components.SoftButton
import com.example.nepsis.ui.components.StatCard
import com.example.nepsis.ui.components.UamBackground
import com.example.nepsis.ui.theme.UamPrimary
import com.example.nepsis.ui.theme.UamTextSecondary

@Composable
fun HomeScreen(
    onIniciarTest: () -> Unit,
    onCerrarSesion: () -> Unit,
    onNavigateToDailyCheckIn: () -> Unit,
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(ServiceLocator.provideNepsisRepository(LocalContext.current))),
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(ServiceLocator.provideProfileRepository(LocalContext.current)))
) {
    val homeState by homeViewModel.state.collectAsState()
    val profileState by profileViewModel.state.collectAsState()
    val perfil = profileState.profile

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
                        titulo = "Hola, ${perfil?.fullName?.split(" ")?.firstOrNull() ?: "Estudiante"}",
                        subtitulo = "Bienvenido a tu panel de orientación"
                    )

                    ModernCard {
                        InfoPill(text = "Usuario identificado")

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = perfil?.fullName ?: "Usuario Nepsis",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = UamPrimary
                        )

                        Text(
                            text = "Nivel: ${perfil?.level ?: 1}",
                            color = UamTextSecondary
                        )

                        Text(
                            text = "Correo: ${perfil?.email ?: "No registrado"}",
                            color = UamTextSecondary
                        )
                    }

                    ModernCard {
                        Text(
                            text = "Estado de Salud Mental",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Tienes ${homeState.moods.size} registros de estado de ánimo. ¡Sigue así!",
                            color = UamTextSecondary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            titulo = "Puntos",
                            valor = perfil?.points?.toString() ?: "0",
                            emoji = "🏆",
                            modifier = Modifier.weight(1f)
                        )

                        StatCard(
                            titulo = "Estado",
                            valor = homeState.moods.firstOrNull()?.emotionalState ?: "Pendiente",
                            emoji = "😊",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    GradientButton(
                        text = "Registro de ánimo diario",
                        onClick = onNavigateToDailyCheckIn
                    )

                    GradientButton(
                        text = "Comenzar nuevo test",
                        onClick = onIniciarTest
                    )

                    SoftButton(
                        text = "Cerrar sesión",
                        onClick = onCerrarSesion
                    )
                }
            }
        }
    }
}