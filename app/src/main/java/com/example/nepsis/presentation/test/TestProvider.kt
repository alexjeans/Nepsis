package com.example.nepsis.presentation.test

data class TestInfo(val title: String, val category: String, val description: String, val duration: String)

object TestProvider {

    fun getTestInfo(testId: String): TestInfo {
        return when (testId) {
            "personalidad" -> TestInfo(
                title = "Test de Personalidad (MBTI)",
                category = "Psicología",
                description = "Descubre tu tipo de personalidad y cómo interactúas con el mundo. Este cuestionario evalúa tus preferencias entre la introversión, extroversión, lógica y emociones.",
                duration = "5 min"
            )
            "lenguaje_amor" -> TestInfo(
                title = "Lenguajes del Amor",
                category = "Relaciones",
                description = "Identifica cómo prefieres dar y recibir aprecio. Basado en los 5 lenguajes: Palabras de afirmación, Tiempo de calidad, Regalos, Actos de servicio y Contacto físico.",
                duration = "3 min"
            )
            else -> TestInfo(
                title = "Test Vocacional",
                category = "Desarrollo Personal",
                description = "Descubre qué áreas profesionales (Tecnología, Humanidades, Artes) se alinean mejor con tus habilidades naturales y pasatiempos.",
                duration = "4 min"
            )
        }
    }

    fun getQuestions(testId: String): List<Question> {
        return when (testId) {
            "personalidad" -> listOf(
                Question(1, "En una reunión social, tú normalmente...", listOf(
                    Option("Hablas con mucha gente, incluso desconocidos", 3),
                    Option("Te quedas con tu grupo pequeño de amigos", 2),
                    Option("Observas y prefieres escuchar", 1)
                )),
                Question(2, "Al tomar una decisión importante, te basas más en...", listOf(
                    Option("La lógica y los hechos objetivos", 3),
                    Option("Un balance entre pros/contras y cómo me siento", 2),
                    Option("Mi intuición y lo que me dicta el corazón", 1)
                )),
                Question(3, "Tu espacio de trabajo/estudio suele estar...", listOf(
                    Option("Perfectamente ordenado y estructurado", 3),
                    Option("Un poco desordenado pero sé dónde está todo", 2),
                    Option("Caótico, me inspira la flexibilidad", 1)
                ))
            )
            "lenguaje_amor" -> listOf(
                Question(1, "Te sientes más apreciado cuando tu pareja/amigo...", listOf(
                    Option("Me dice lo mucho que me valora", 3),
                    Option("Pasa tiempo a solas conmigo sin distracciones", 2),
                    Option("Me sorprende con un detalle o regalo", 1)
                )),
                Question(2, "Cuando quieres animar a alguien que quieres, tú...", listOf(
                    Option("Le doy un abrazo fuerte", 3),
                    Option("Le ofrezco mi ayuda práctica (Actos de servicio)", 2),
                    Option("Le escribo un mensaje bonito", 1)
                )),
                Question(3, "Lo que más te lastimaría en una relación es...", listOf(
                    Option("La crítica destructiva y palabras hirientes", 3),
                    Option("Que cancelen nuestros planes a última hora", 2),
                    Option("La frialdad física y falta de contacto", 1)
                ))
            )
            else -> listOf(
                Question(1, "¿Qué actividad disfrutas más en tu tiempo libre?", listOf(
                    Option("Armar computadoras, programar o resolver lógica", 3),
                    Option("Leer sobre comportamiento humano o ayudar a otros", 2),
                    Option("Dibujar, diseñar o crear contenido", 1)
                )),
                Question(2, "¿Cómo reaccionas ante un problema complejo?", listOf(
                    Option("Analizo los datos y busco el error metódicamente", 3),
                    Option("Hablo con mi equipo para hacer una lluvia de ideas", 2),
                    Option("Busco una solución creativa y fuera de lo común", 1)
                )),
                Question(3, "¿Qué tipo de revistas o videos sueles consumir?", listOf(
                    Option("Tecnología, ciencia o innovaciones", 3),
                    Option("Psicología, documentales o sociedad", 2),
                    Option("Arte, cine, moda o diseño", 1)
                ))
            )
        }
    }

    fun getResult(testId: String, score: Int): String {
        return when (testId) {
            "personalidad" -> if (score >= 7) "Extrovertido y Analítico" else "Introvertido y Emocional"
            "lenguaje_amor" -> if (score >= 7) "Palabras de Afirmación y Tiempo de Calidad" else "Contacto Físico y Actos de Servicio"
            // CRASH SOLUCIONADO: Se cambió la barra "/" por un guion "-"
            else -> if (score >= 7) "Perfil Tecnológico - Ingeniería" else "Perfil Humanista - Creativo"
        }
    }
}