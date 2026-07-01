# Nepsis

Nepsis es una aplicación Android de autoconocimiento y bienestar emocional. Su objetivo es convertirse en un espacio donde el usuario pueda conocerse mejor mediante tests, historial estructurado y asistencia por inteligencia artificial. Todo gira en torno a un "Súper Perfil" dinámico, manteniendo un diseño minimalista, intuitivo y sin mecánicas de gamificación.

## Tecnologías

- Kotlin & Jetpack Compose
- Clean Architecture (MVVM)
- Room Database (Persistencia Local & Offline-First)
- Retrofit & Supabase (Backend, BD Relacional y Sincronización)
- DataStore (Preferencias, Onboarding y Modo Oscuro)
- Credential Manager (Google Login)
- Navigation Compose (Transiciones)
- WorkManager (Notificaciones en segundo plano)
- SDK Google Generative AI (Gemini 1.5 Flash)

## Cómo ejecutar

1. Clonar el repositorio
2. Configurar el archivo `local.properties` con la variable `GEMINI_API_KEY=tu_api_key`
3. Abrir el proyecto en Android Studio
4. Sincronizar Gradle
5. Ejecutar la app en emulador o dispositivo Android

## Roadmap

### Fase 0: Cimientos y Arquitectura (Completado)
| Funcionalidad | Estado |
|---|---|
| Estructura Clean Architecture (MVVM) | ✅ |
| Base de Datos Local (Room configurado) | ✅ |
| Conexión Backend (Supabase + Retrofit) | ✅ |
| Login base con Google (Credential Manager) | ✅ |
| Navegación centralizada (AppDestinations) | ✅ |
| Flujo interactivo de Tests (MVP) | ✅ |

### Fase 1: Autenticación y Onboarding (Completado)
| Funcionalidad | Estado |
|---|---|
| Mejora UI Login (Correo/Contraseña visual) | ✅ |
| Control de Primer Ingreso (DataStore) | ✅ |
| Onboarding: Bienvenida | ✅ |
| Onboarding: Información Básica | ✅ |
| Onboarding: Objetivo Principal | ✅ |
| Redirección inteligente (NavHost) | ✅ |

### Fase 2: El Súper Perfil e Historial (Completado)
| Funcionalidad | Estado |
|---|---|
| Limpieza de BottomBar (Inicio, Tests, Perfil) | ✅ |
| Cabecera dinámica de Perfil (Datos Onboarding)| ✅ |
| Módulos de Tests Dinámicos y Reales en Perfil | ✅ |
| Historial Integrado aislado por Usuario | ✅ |
| Cierre de sesión seguro (Limpieza BD y DataStore) | ✅ |
| Pantalla de Configuración base | ✅ |

### Fase 3: UX y Diseño Moderno (En progreso)
| Funcionalidad | Estado |
|---|---|
| Aislamiento de Daily Check-in por Usuario | ✅ |
| Transiciones suaves de navegación (Slide/Fade) | ✅ |
| Tema Dinámico (Modo Oscuro global vía DataStore) | ✅ |
| Notificaciones Locales Diarias (WorkManager) | ✅ |
| Splash Screen Nativo y Launcher Icon | ⏳ |
| Skeletons de carga (Efecto Shimmer) | ❌ |
| Pull to Refresh en Inicio/Perfil | ❌ |
| Snackbars de feedback visual | ❌ |

### Fase 4: Escalabilidad de Tests (Completado)
| Funcionalidad | Estado |
|---|---|
| Estructura genérica JSON en Room | ✅ |
| Arquitectura para descargar tests vía Supabase | ✅ |
| Racha Diaria (Retención no gamificada) | ❌ |

### Fase 5 y 6: Inteligencia Artificial y Nube (En progreso)
| Funcionalidad | Estado |
|---|---|
| Asistente IA Contextual (Gemini) en toda la app | ✅ |
| Sincronización offline estricta (Patrón Repositorio) | ✅ |
| Análisis cruzado profundo de resultados con IA | ⏳ |
| Autenticación propia real (Supabase Auth) |
