# 🌍 Reloj Mundial Multiplataforma

Una aplicación de reloj mundial moderna y elegante desarrollada con **Kotlin Multiplatform** y **Compose Multiplatform** que te permite visualizar las horas de diferentes ciudades del mundo en tiempo real.

## ✨ Características Principales

### 🌆 Cobertura Global
- **170+ ciudades** en 6 continentes
- Zonas horarias precisas con detección automática
- Hora local siempre visible
- Soporte para ciudades populares y personalizadas

### 🎨 Interfaz Moderna
- **Material Design 3** con tema claro/oscuro automático
- Animaciones fluidas y microinteracciones
- Indicadores visuales de día/noche
- Banderas de países para identificación rápida

### ⚙️ Personalización Avanzada
- Formato de 12/24 horas configurable
- Mostrar/ocultar segundos y fecha
- Diferencia horaria con hora local
- Sistema de favoritos y ordenamiento

### 🔍 Búsqueda Inteligente
- Búsqueda en tiempo real por ciudad o país
- Historial de búsquedas reciente
- Filtros avanzados (favoritos, populares, personalizados)

### 💾 Almacenamiento Persistente
- Guardado automático de preferencias
- Exportación/importación de configuración
- Sincronización multiplataforma

## 🖥️ Plataformas Soportadas

| Plataforma | Estado | Características |
|------------|--------|----------------|
| **Android** | ✅ Completo | Material Design 3 nativo |
| **iOS** | ✅ Completo | Interfaz adaptada a iOS |
| **Desktop** | ✅ Completo | Windows, macOS, Linux |
| **Web** | ✅ Completo | Navegadores modernos (Wasm/JS) |

## 📱 Capturas de Pantalla

### Aplicación Principal
```
[Aquí irían las capturas de pantalla de la aplicación]
- Lista de relojes con diferentes zonas horarias
- Interfaz de búsqueda y añadir ciudades
- Pantalla de configuración y personalización
```

### Multiplataforma
```
[Capturas mostrando la misma app en diferentes plataformas]
- Android: Interfaz nativa con navegación inferior
- Desktop: Ventana redimensionable con menú tradicional
- Web: Versión responsiva en navegador
- iOS: Diseño adaptado a convenciones de iOS
```

## 🏗️ Arquitectura Técnica

### Estructura del Proyecto
```
FirstKMPApp/
├── composeApp/
│   ├── src/
│   │   ├── commonMain/kotlin/     # Código compartido (90%)
│   │   ├── androidMain/kotlin/     # Específico de Android
│   │   ├── iosMain/kotlin/         # Específico de iOS
│   │   ├── jvmMain/kotlin/         # Desktop (JVM)
│   │   ├── jsMain/kotlin/          # Web JavaScript
│   │   └── wasmJsMain/kotlin/      # Web WebAssembly
│   └── build.gradle.kts
├── iosApp/                         # Entry point iOS
├── build.gradle.kts               # Configuración raíz
└── README.md
```

### Componentes Principales

#### 🕐 **ClockManager.kt**
Gestor central de relojes y preferencias
- Manejo de lista de relojes mundiales
- Operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
- Sistema de favoritos y ordenamiento
- Gestión de preferencias de usuario

#### ⏰ **TimeService.kt**
Servicio para cálculos de tiempo y zonas horarias
- Conversión precisa de zonas horarias
- Formato flexible de tiempo (12/24 horas)
- Cálculo de diferencias horarias
- Detección automática de día/noche

#### 🌍 **Location.kt**
Base de datos geográfica mundial
- 170+ ciudades predefinidas
- Sistema de búsqueda y filtrado
- Ubicaciones populares preseleccionadas
- Soporte para ubicaciones personalizadas

#### 💾 **StorageManager.kt**
Almacenamiento persistente multiplataforma
- Serialización JSON con Kotlinx Serialization
- Almacenamiento con Multiplatform Settings
- Sistema de importación/exportación
- Manejo robusto de errores

#### 🎨 **App.kt & UI Components**
Interfaz de usuario con Compose Multiplatform
- Diseño declarativo moderno
- Estado reactivo con StateFlow
- Animaciones y transiciones fluidas
- Tema adaptable claro/oscuro

## 🛠️ Tecnologías Utilizadas

### **Core Framework**
- **Kotlin Multiplatform**: Base multiplataforma
- **Compose Multiplatform**: UI declarativa
- **Kotlinx DateTime**: Manejo de fechas y tiempo
- **Kotlinx Serialization**: Serialización JSON

### **Almacenamiento**
- **Multiplatform Settings**: Almacenamiento persistente
- **Gson**: Procesamiento JSON (fallback)

### **UI/UX**
- **Material Design 3**: Sistema de diseño
- **Material Icons Extended**: Iconos variados
- **Compose Animations**: Animaciones fluidas

### **Plataformas Específicas**
- **Android**: Material Design 3, Activity Compose
- **iOS**: Interoperabilidad con SwiftUI
- **Desktop**: Soporte para ventanas nativas
- **Web**: WebAssembly y JavaScript moderno

## 🚀 Instalación y Ejecución

### Prerrequisitos

- **JDK 11** o superior
- **Android Studio** (para desarrollo Android)
- **Xcode** (para desarrollo iOS, solo macOS)
- **Git** para clonar el repositorio

### Clonar el Proyecto

```bash
git clone https://github.com/tu-usuario/FirstKMPApp.git
cd FirstKMPApp
```

### 📱 Android

**Requisitos:**
- Android Studio Arctic Fox o superior
- Android SDK API 24+ (Android 7.0)

**Ejecución:**
```bash
# Desde terminal
./gradlew :composeApp:assembleDebug

# O ejecutar directamente
./gradlew :composeApp:installDebug
```

**Desde Android Studio:**
1. Abrir el proyecto en Android Studio
2. Seleccionar configuración de ejecución "androidApp"
3. Presionar Run (▶️) o usar el shortcut Shift+F10

### 🖥️ Desktop (Windows, macOS, Linux)

**Ejecución:**
```bash
# macOS/Linux
./gradlew :composeApp:run

# Windows
.\gradlew.bat :composeApp:run
```

**Generar ejecutable:**
```bash
# Genera .exe, .dmg, .deb
./gradlew :composeApp:packageDistributionForCurrentOS
```

### 🌐 Web

**Opción 1: WebAssembly (Recomendado - Navegadores modernos)**
```bash
# macOS/Linux
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Windows
.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
```

**Opción 2: JavaScript (Compatibilidad con navegadores antiguos)**
```bash
# macOS/Linux
./gradlew :composeApp:jsBrowserDevelopmentRun

# Windows
.\gradlew.bat :composeApp:jsBrowserDevelopmentRun
```

**Producción:**
```bash
# Generar archivos para despliegue
./gradlew :composeApp:wasmJsBrowserProductionBuild
```

### 🍎 iOS

**Requisitos:**
- macOS con Xcode 14+
- iOS Simulator o dispositivo físico

**Ejecución:**
```bash
# Abrir proyecto en Xcode
open iosApp/iosApp.xcworkspace

# O desde terminal (requiere Xcode)
xcodebuild -workspace iosApp/iosApp.xcworkspace -scheme iosApp -destination 'platform=iOS Simulator,name=iPhone 14'
```

**Desde Xcode:**
1. Abrir `iosApp/iosApp.xcworkspace`
2. Seleccionar target o simulador
3. Presionar Run (▶️)

## 🎯 Uso de la Aplicación

### Primeros Pasos

1. **Iniciar la aplicación** en tu plataforma preferida
2. **Explorar relojes predefinidos** de ciudades populares
3. **Añadir nuevas ciudades** usando el botón ➕
4. **Personalizar la vista** desde Ajustes (⚙️)

### Funcionalidades Clave

#### 🕐 **Gestión de Relojes**
- **Añadir**: Busca ciudades por nombre o país
- **Favoritos**: Marca ⭐ para acceso rápido
- **Eliminar**: Desliza o presiona ❌ (excepto hora local)
- **Reordenar**: Arrastra para cambiar el orden

#### 🎨 **Personalización**
- **Formato hora**: Cambia entre 12/24 horas
- **Visualización**: Activa/segundos, fecha, diferencia horaria
- **Tema**: Claro, oscuro o automático
- **Indicadores**: Día/noche, banderas, etc.

#### 🔍 **Búsqueda**
- **Búsqueda rápida**: Escribe nombre de ciudad o país
- **Historial**: Accede a búsquedas recientes
- **Filtros**: Por favoritos, populares o personalizados

#### 💾 **Datos**
- **Exportar**: Guarda configuración actual
- **Importar**: Restaura desde backup
- **Sincronización**: Datos persisten entre sesiones

## 🔧 Configuración Avanzada

### Preferencias de Usuario

La aplicación ofrece configuraciones detalladas:

```kotlin
// Ejemplo de preferencias configurables
UserPreferences(
    is24HourFormat = true,           // Formato 24 horas
    showSeconds = true,              // Mostrar segundos
    showDate = true,                 // Mostrar fecha
    showTimeDifference = true,       // Diferencia horaria
    showCountryFlags = true,         // Banderas de países
    showDayNightIndicator = true,    // Indicador día/noche
    autoRefresh = true,              // Actualización automática
    refreshInterval = 1,             // Intervalo en segundos
    themeMode = "auto",              // Tema: light/dark/auto
    enableNotifications = false       // Notificaciones (futuro)
)
```

### Arquitectura de Datos

```kotlin
// Estructura de datos principal
data class WorldClock(
    val id: String,           // Identificador único
    val location: Location,   // Información geográfica
    val isFavorite: Boolean,  // Es favorito
    val isLocalTime: Boolean, // Es hora local
    val order: Int           // Orden en la lista
)

data class Location(
    val country: String,      // País
    val city: String,         // Ciudad
    val timezoneId: String,   // ID de zona horaria
    val utcOffsetHours: Int   // Offset UTC
)
```

## 🤝 Contribución

¡Contribuciones son bienvenidas! Por favor:

1. **Fork** el repositorio
2. **Crear** una rama (`git checkout -b feature/amazing-feature`)
3. **Commit** tus cambios (`git commit -m 'Add amazing feature'`)
4. **Push** a la rama (`git push origin feature/amazing-feature`)
5. **Abrir** un Pull Request

### Guía de Estilo

- **Kotlin**: Seguir convenciones oficiales
- **Compose**: Usar principios de UI declarativa
- **Nomenclatura**: Nombres descriptivos en inglés
- **Comentarios**: Documentar funciones complejas

## 📄 Licencia

Este proyecto está licenciado bajo la **MIT License** - ver el archivo [LICENSE](LICENSE) para detalles.

## 🙏 Agradecimientos

- **JetBrains** por Kotlin Multiplatform y Compose
- **Google** por Material Design 3
- **Comunidad KMP** por ejemplos y soporte

## 📚 Recursos y Aprendizaje

### Documentación Oficial
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Material Design 3](https://m3.material.io/)
- [Kotlinx DateTime](https://github.com/Kotlin/kotlinx-datetime)

### Tutoriales y Ejemplos
- [Compose Multiplatform Samples](https://github.com/JetBrains/compose-multiplatform)
- [KMP Guide](https://kotlinlang.org/docs/multiplatform-get-started.html)
- [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings)

## 📞 Contacto

- **Autores**: Alejandro Perez Vazquez
- **Proyecto**: Programación Móvil II


---

**⭐ Si te gusta este proyecto, ¡dale una estrella!**

**🔄 Última actualización**: Mayo 2026
