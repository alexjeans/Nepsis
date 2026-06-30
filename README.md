# Nepsis

Nepsis es una aplicación Android de autoconocimiento y bienestar emocional. Su objetivo es convertirse en un espacio donde el usuario pueda conocerse mejor mediante tests, historial estructurado y, a futuro, inteligencia artificial. Todo gira en torno a un "Súper Perfil" dinámico, manteniendo un diseño minimalista, intuitivo y sin mecánicas de gamificación.

## Tecnologías

- Kotlin & Jetpack Compose
- Clean Architecture (MVVM)
- Room Database (Persistencia Local)
- Retrofit & Supabase (Backend y Sincronización)
- DataStore (Preferencias y Onboarding)
- Credential Manager (Google Login)
- Navigation Compose

## Cómo ejecutar

1. Clonar el repositorio
2. Abrir el proyecto en Android Studio
3. Sincronizar Gradle
4. Ejecutar la app en emulador o dispositivo Android

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

### Fase 2: El Súper Perfil (Completado)
| Funcionalidad | Estado |
|---|---|
| Limpieza de BottomBar (Inicio, Tests, Perfil) | ✅ |
| Cabecera dinámica de Perfil (Datos Onboarding)| ✅ |
| Módulos de Tests Dinámicos en Perfil | ✅ |
| Historial Integrado como sub-sección | ✅ |
| Cierre de sesión (Limpieza BD y DataStore) | ✅ |
| Pantalla de Configuración base | ✅ |

### Fase 3: UX y Diseño Moderno (En progreso)
| Funcionalidad | Estado |
|---|---|
| Splash Screen Nativo y Launcher Icon | ⏳ |
| Skeletons de carga (Efecto Shimmer) | ⏳ |
| Transiciones suaves entre pantallas | ⏳ |
| Pull to Refresh en Inicio/Perfil | ⏳ |
| Snackbars de feedback visual | ⏳ |

### Fase 4: Escalabilidad de Tests (Futuro)
| Funcionalidad | Estado |
|---|---|
| Estructura genérica JSON en Room | ❌ |
| Arquitectura para nuevos tests (MBTI, etc.) | ❌ |
| Racha Diaria (Retención no gamificada) | ❌ |

### Fase 5 y 6: Inteligencia Artificial y Nube (Visión a Largo Plazo)
| Funcionalidad | Estado |
|---|---|
| Análisis cruzado de resultados con IA | ❌ |
| Recomendaciones personalizadas | ❌ |
| Autenticación propia real (Supabase Auth) | ❌ |
| Sincronización offline estricta | ❌ |
| Exportación de perfil y datos (PDF/JSON) | ❌ |
