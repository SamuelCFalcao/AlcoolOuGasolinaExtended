// ui/theme/Theme.kt
package com.example.alcoolgasolina.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Cores customizadas para o tema claro
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006B3D), // Verde escuro (gasolina/natureza)
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB8F4D6), // Verde claro
    onPrimaryContainer = Color(0xFF002110),

    secondary = Color(0xFF4D6357),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD0E8D9),
    onSecondaryContainer = Color(0xFF0B1F16),

    tertiary = Color(0xFF3D6373), // Azul petróleo
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFC1E8FB),
    onTertiaryContainer = Color(0xFF001F29),

    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color.White,
    onErrorContainer = Color(0xFF410002),

    background = Color(0xFFFBFDF8),
    onBackground = Color(0xFF191C1A),

    surface = Color(0xFFFBFDF8),
    onSurface = Color(0xFF191C1A),
    surfaceVariant = Color(0xFFDCE5DC),
    onSurfaceVariant = Color(0xFF404943)
)

// Cores customizadas para o tema escuro
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9DD7BB), // Verde menta
    onPrimary = Color(0xFF00391D),
    primaryContainer = Color(0xFF00522C),
    onPrimaryContainer = Color(0xFFB8F4D6),

    secondary = Color(0xFFB4CCBE),
    onSecondary = Color(0xFF20352A),
    secondaryContainer = Color(0xFF364B40),
    onSecondaryContainer = Color(0xFFD0E8D9),

    tertiary = Color(0xFFA5CCDE), // Azul claro
    onTertiary = Color(0xFF073543),
    tertiaryContainer = Color(0xFF244C5B),
    onTertiaryContainer = Color(0xFFC1E8FB),

    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),

    background = Color(0xFF191C1A),
    onBackground = Color(0xFFE1E3DE),

    surface = Color(0xFF191C1A),
    onSurface = Color(0xFFE1E3DE),
    surfaceVariant = Color(0xFF404943),
    onSurfaceVariant = Color(0xFFC0C9C1)
)

@Composable
fun AlcoolGasolinaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desabilitado para usar nossas cores customizadas
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}