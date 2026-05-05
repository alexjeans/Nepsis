package com.example.nepsis.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nepsis.model.PerfilEstudiante
import com.example.nepsis.ui.components.GradientButton
import com.example.nepsis.ui.components.InfoPill
import com.example.nepsis.ui.components.ModernCard
import com.example.nepsis.ui.components.UamBackground
import com.example.nepsis.ui.theme.UamPrimary
import com.example.nepsis.ui.theme.UamTextSecondary

@Composable
fun LoginScreen(
    onIngresar: (PerfilEstudiante) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var colegio by remember { mutableStateOf("") }
    var intento by remember { mutableStateOf(false) }

    val nombreValido = nombre.trim().length >= 3
    val correoValido = correo.contains("@") && correo.contains(".")
    val colegioValido = colegio.trim().length >= 3
    val formularioValido = nombreValido && correoValido && colegioValido

    UamBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 })
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "UAM",
                        color = UamPrimary,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black
                    )

                    Text(
                        text = "Nepsis Vocacional",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Orientación profesional para futuros universitarios",
                        color = UamTextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    ModernCard {
                        InfoPill(text = "Evaluación de intereses vocacionales")

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Datos del estudiante",
                            style = MaterialTheme.typography.titleLarge,
                            color = UamPrimary,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Completa la información para iniciar el test.",
                            color = UamTextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre completo") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = intento && !nombreValido,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = UamPrimary,
                                focusedLabelColor = UamPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = { Text("Correo") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = intento && !correoValido,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = UamPrimary,
                                focusedLabelColor = UamPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = colegio,
                            onValueChange = { colegio = it },
                            label = { Text("Colegio o institución") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = intento && !colegioValido,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = UamPrimary,
                                focusedLabelColor = UamPrimary
                            )
                        )

                        if (intento && !formularioValido) {
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Revisa los campos antes de continuar.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        GradientButton(
                            text = "Iniciar experiencia",
                            onClick = {
                                intento = true

                                if (formularioValido) {
                                    onIngresar(
                                        PerfilEstudiante(
                                            nombre = nombre,
                                            correo = correo,
                                            colegio = colegio
                                        )
                                    )
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Esta aplicación es educativa y orientativa. No sustituye una evaluación psicológica profesional.",
                        textAlign = TextAlign.Center,
                        color = UamTextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}