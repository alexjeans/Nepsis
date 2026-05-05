package com.example.nepsis.data

import com.example.nepsis.model.AreaVocacional
import com.example.nepsis.model.OpcionVocacional
import com.example.nepsis.model.PreguntaVocacional
import com.example.nepsis.model.TestVocacional

object VocacionalRepository {

    val modulosPropuestos = listOf(
        "Evaluación del Coeficiente Intelectual",
        "Evaluación del Esquema de Valores",
        "Evaluación de Intereses Vocacionales",
        "Evaluación de Aptitudes"
    )

    val testVocacional = TestVocacional(
        titulo = "Test de Intereses Vocacionales",
        modulo = "Evaluación de Intereses Vocacionales",
        descripcion = "Instrumento educativo inspirado en la ruta de colaboración Ingeniería en Sistemas - Psicología.",
        preguntas = listOf(
            PreguntaVocacional(
                enunciado = "En una feria universitaria, ¿qué actividad elegirías primero?",
                opciones = listOf(
                    OpcionVocacional("Probar una app, robot o prototipo tecnológico", AreaVocacional.TECNOLOGIA),
                    OpcionVocacional("Hablar con estudiantes de medicina o psicología", AreaVocacional.SALUD),
                    OpcionVocacional("Participar en una dinámica de liderazgo", AreaVocacional.SOCIAL),
                    OpcionVocacional("Ver ideas de negocios o emprendimientos", AreaVocacional.ADMINISTRACION)
                )
            ),
            PreguntaVocacional(
                enunciado = "Cuando trabajas en equipo, normalmente prefieres:",
                opciones = listOf(
                    OpcionVocacional("Programar, construir o resolver la parte técnica", AreaVocacional.TECNOLOGIA),
                    OpcionVocacional("Escuchar y apoyar a quienes tienen dificultades", AreaVocacional.SALUD),
                    OpcionVocacional("Explicar, comunicar o mediar entre compañeros", AreaVocacional.SOCIAL),
                    OpcionVocacional("Coordinar tareas, fechas y responsabilidades", AreaVocacional.ADMINISTRACION)
                )
            ),
            PreguntaVocacional(
                enunciado = "¿Qué tipo de problema te gustaría resolver en el futuro?",
                opciones = listOf(
                    OpcionVocacional("Crear soluciones digitales para la sociedad", AreaVocacional.TECNOLOGIA),
                    OpcionVocacional("Mejorar la salud física o emocional de las personas", AreaVocacional.SALUD),
                    OpcionVocacional("Reducir problemas sociales o educativos", AreaVocacional.SOCIAL),
                    OpcionVocacional("Ayudar a empresas o proyectos a crecer", AreaVocacional.ADMINISTRACION)
                )
            ),
            PreguntaVocacional(
                enunciado = "¿Cuál materia se acerca más a tus gustos?",
                opciones = listOf(
                    OpcionVocacional("Informática, matemática o física", AreaVocacional.TECNOLOGIA),
                    OpcionVocacional("Biología, química o educación para la salud", AreaVocacional.SALUD),
                    OpcionVocacional("Historia, literatura, filosofía o convivencia", AreaVocacional.SOCIAL),
                    OpcionVocacional("Economía, contabilidad o emprendimiento", AreaVocacional.ADMINISTRACION)
                )
            ),
            PreguntaVocacional(
                enunciado = "Si tuvieras tiempo libre para aprender algo nuevo, elegirías:",
                opciones = listOf(
                    OpcionVocacional("Diseñar páginas web o aplicaciones", AreaVocacional.TECNOLOGIA),
                    OpcionVocacional("Aprender sobre salud mental o primeros auxilios", AreaVocacional.SALUD),
                    OpcionVocacional("Participar en voluntariados o tutorías", AreaVocacional.SOCIAL),
                    OpcionVocacional("Aprender ventas, liderazgo o finanzas", AreaVocacional.ADMINISTRACION)
                )
            ),
            PreguntaVocacional(
                enunciado = "¿Qué actividad te representa mejor?",
                opciones = listOf(
                    OpcionVocacional("Analizar datos y buscar patrones", AreaVocacional.CIENCIAS),
                    OpcionVocacional("Dibujar, editar videos o crear contenido", AreaVocacional.CREATIVIDAD),
                    OpcionVocacional("Ayudar a alguien a tomar una decisión personal", AreaVocacional.SOCIAL),
                    OpcionVocacional("Organizar un plan para lograr una meta", AreaVocacional.ADMINISTRACION)
                )
            )
        )
    )
}