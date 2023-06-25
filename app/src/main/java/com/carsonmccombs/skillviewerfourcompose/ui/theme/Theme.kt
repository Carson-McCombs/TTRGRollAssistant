package com.carsonmccombs.skillviewerfourcompose.ui.theme

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CustomDarkColorScheme = darkColorScheme(

    primary = ChinesePurple80,
    onPrimary = ChinesePurple20,
    primaryContainer = ChinesePurple40,
    onPrimaryContainer = ChinesePurple90,
    inversePrimary = ChinesePurple40,

    secondary = PowderBlue80,
    onSecondary = PowderBlue20,
    secondaryContainer = PowderBlue30,
    onSecondaryContainer = PowderBlue90,

    tertiary = IceBlue80,
    onTertiary = IceBlue20,
    tertiaryContainer = IceBlue30,
    onTertiaryContainer = IceBlue90,

    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,

    background = Grey10,
    onBackground = Grey90,
    surface = Grey30,
    onSurface = Grey80,
    inverseSurface = Grey90,
    inverseOnSurface = Grey10,

    surfaceVariant = Grey30,
    onSurfaceVariant = Grey80,
    outline = Grey80
)
private val CustomLightColorScheme = lightColorScheme(

    primary = ChinesePurple40,
    onPrimary = ChinesePurple90,
    primaryContainer = ChinesePurple40,
    onPrimaryContainer = ChinesePurple90,
    inversePrimary = ChinesePurple40,

    secondary = PowderBlue20,
    onSecondary = PowderBlue80,
    secondaryContainer = PowderBlue90,
    onSecondaryContainer = PowderBlue30,

    tertiary = IceBlue20,
    onTertiary = IceBlue80,
    tertiaryContainer = IceBlue90,
    onTertiaryContainer = IceBlue30,

    error = Red20,
    onError = Red80,
    errorContainer = Red90,
    onErrorContainer = Red30,

    background = Grey80,
    onBackground = Grey10,
    surface = Grey80,
    onSurface = Grey30,
    inverseSurface = Grey10,
    inverseOnSurface = Grey90,

    surfaceVariant = Grey80,
    onSurfaceVariant = Grey30,
    outline = Grey30
)



@Composable
fun SkillViewerFourComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> CustomDarkColorScheme
        else -> CustomLightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}