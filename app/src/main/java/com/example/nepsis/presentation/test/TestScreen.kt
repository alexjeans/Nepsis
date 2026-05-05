package com.example.nepsis.presentation.test

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nepsis.model.OpcionVocacional
import com.example.nepsis.model.PreguntaVocacional
import com.example.nepsis.model.TestVocacional
import com.example.nepsis.ui.components.GradientButton
import com.example.nepsis.ui.components.InfoPill
import com.example.nepsis.ui.components.ModernCard
import com.example.nepsis.ui.components.SoftButton
import com.example.nepsis.ui.components.UamBackground
import com.example.nepsis.ui.theme.UamAccent
import com.example.nepsis.ui.theme.UamPrimary
import com.example.nepsis.ui.theme.UamTextSecondary

@Composable
fun TestScreen(
    test: TestVocacional,
    respuestas: Map<Int, OpcionVocacional>,
    onResponder: (Int, OpcionVocacional) -> Unit,
    onVolver: () -> Unit,
    onFinalizar: () -> Unit
) {
    val totalPreguntas = test.preguntas.size
    val respondidas = respuestas.size
    val progreso = respondidas.toFloat() / totalPreguntas.toFloat()
    val puedeFinalizar = respondidas == totalPreguntas

    UamBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ModernCard {
                InfoPill(text = test.modulo)

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = test.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = UamPrimary
                )

                Text(
                    text = "Responde con sinceridad para obtener una orientación más útil.",
                    color = UamTextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Progreso: $respondidas de $totalPreguntas preguntas",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = progreso,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = UamPrimary,
                    trackColor = UamAccent
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                itemsIndexed(test.preguntas) { index, pregunta ->
                    PreguntaCardModerna(
                        numero = index + 1,
                        pregunta = pregunta,
                        respuestaSeleccionada = respuestas[index],
                        onSeleccionar = { opcion ->
                            onResponder(index, opcion)
                        }
                    )
                }
            }

            GradientButton(
                text = "Ver resultado vocacional",
                onClick = onFinalizar,
                enabled = puedeFinalizar
            )

            SoftButton(
                text = "Volver al inicio",
                onClick = onVolver
            )
        }
    }
}

@Composable
fun PreguntaCardModerna(
    numero: Int,
    pregunta: PreguntaVocacional,
    respuestaSeleccionada: OpcionVocacional?,
    onSeleccionar: (OpcionVocacional) -> Unit
) {
    ModernCard {
        InfoPill(text = "Pregunta $numero")

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = pregunta.enunciado,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        pregunta.opciones.forEach { opcion ->
            OpcionModerna(
                opcion = opcion,
                seleccionada = respuestaSeleccionada == opcion,
                onSeleccionar = {
                    onSeleccionar(opcion)
                }
            )
        }
    }
}

@Composable
fun OpcionModerna(
    opcion: OpcionVocacional,
    seleccionada: Boolean,
    onSeleccionar: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (seleccionada) UamAccent else Color(0xFFF7FAFB),
        label = "color_opcion"
    )

    val escala by animateFloatAsState(
        targetValue = if (seleccionada) 1.02f else 1f,
        label = "escala_opcion"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .scale(escala)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onSeleccionar()
            }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = seleccionada,
            onClick = onSeleccionar,
            colors = RadioButtonDefaults.colors(
                selectedColor = UamPrimary
            )
        )

        Text(
            text = opcion.texto,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}