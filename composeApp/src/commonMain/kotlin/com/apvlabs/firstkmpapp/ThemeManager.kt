package com.apvlabs.firstkmpapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// Define color schemes
val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD3E3FD),
    onPrimaryContainer = Color(0xFF001D36),
    
    secondary = Color(0xFF535E70),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD7E3F7),
    onSecondaryContainer = Color(0xFF101C2B),
    
    tertiary = Color(0xFF6B5778),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF2DAFF),
    onTertiaryContainer = Color(0xFF251431),
    
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    
    background = Color(0xFFFDFBFF),
    onBackground = Color(0xFF1A1B1E),
    surface = Color(0xFFFDFBFF),
    onSurface = Color(0xFF1A1B1E),
    surfaceVariant = Color(0xFFE0E2EC),
    onSurfaceVariant = Color(0xFF44474F),
    
    outline = Color(0xFF74777F),
    outlineVariant = Color(0xFFC4C6CF),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF2F3033),
    inverseOnSurface = Color(0xFFF1F0F4),
    inversePrimary = Color(0xFFA3CAFD),
    surfaceTint = Color(0xFF1976D2),
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFA3CAFD),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF00497D),
    onPrimaryContainer = Color(0xFFD3E3FD),
    
    secondary = Color(0xFFBBC7DB),
    onSecondary = Color(0xFF253140),
    secondaryContainer = Color(0xFF3B4858),
    onSecondaryContainer = Color(0xFFD7E3F7),
    
    tertiary = Color(0xFFD6BFEB),
    onTertiary = Color(0xFF3A2948),
    tertiaryContainer = Color(0xFF513F5F),
    onTertiaryContainer = Color(0xFFF2DAFF),
    
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    
    background = Color(0xFF121316),
    onBackground = Color(0xFFE2E2E5),
    surface = Color(0xFF121316),
    onSurface = Color(0xFFE2E2E5),
    surfaceVariant = Color(0xFF44474F),
    onSurfaceVariant = Color(0xFFC4C6CF),
    
    outline = Color(0xFF8E9199),
    outlineVariant = Color(0xFF44474F),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFE2E2E5),
    inverseOnSurface = Color(0xFF2F3033),
    inversePrimary = Color(0xFF1976D2),
    surfaceTint = Color(0xFFA3CAFD),
)

// Theme modes
enum class ThemeMode {
    LIGHT, DARK, AUTO
}

// Composition local for theme mode
val LocalThemeMode = compositionLocalOf { ThemeMode.AUTO }

// Theme manager object
object ThemeManager {
    private var _themeMode = ThemeMode.AUTO
    
    fun getThemeMode(): ThemeMode = _themeMode
    fun setThemeMode(mode: ThemeMode) {
        _themeMode = mode
    }
}

@Composable
fun WorldClockTheme(
    themeMode: ThemeMode = ThemeManager.getThemeMode(),
    content: @Composable () -> Unit
) {
    val systemInDarkTheme = isSystemInDarkTheme()
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.AUTO -> systemInDarkTheme
    }
    
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    
    CompositionLocalProvider(
        LocalThemeMode provides themeMode
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MaterialTheme.typography,
            content = content
        )
    }
}

// Helper functions to get current theme info
@Composable
fun getCurrentThemeMode(): ThemeMode = LocalThemeMode.current

@Composable
fun isDarkTheme(): Boolean {
    val themeMode = getCurrentThemeMode()
    val systemInDarkTheme = isSystemInDarkTheme()
    return when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.AUTO -> systemInDarkTheme
    }
}
