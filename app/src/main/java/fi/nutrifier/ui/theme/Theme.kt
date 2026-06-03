package fi.nutrifier.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

val themeColorsInUse = Blue2ThemeColors

private val lightScheme = lightColorScheme(
    primary = themeColorsInUse.primaryLight,
    onPrimary = themeColorsInUse.onPrimaryLight,
    primaryContainer = themeColorsInUse.primaryContainerLight,
    onPrimaryContainer = themeColorsInUse.onPrimaryContainerLight,
    secondary = themeColorsInUse.secondaryLight,
    onSecondary = themeColorsInUse.onSecondaryLight,
    secondaryContainer = themeColorsInUse.secondaryContainerLight,
    onSecondaryContainer = themeColorsInUse.onSecondaryContainerLight,
    tertiary = themeColorsInUse.tertiaryLight,
    onTertiary = themeColorsInUse.onTertiaryLight,
    tertiaryContainer = themeColorsInUse.tertiaryContainerLight,
    onTertiaryContainer = themeColorsInUse.onTertiaryContainerLight,
    error = themeColorsInUse.errorLight,
    onError = themeColorsInUse.onErrorLight,
    errorContainer = themeColorsInUse.errorContainerLight,
    onErrorContainer = themeColorsInUse.onErrorContainerLight,
    background = themeColorsInUse.backgroundLight,
    onBackground = themeColorsInUse.onBackgroundLight,
    surface = themeColorsInUse.surfaceLight,
    onSurface = themeColorsInUse.onSurfaceLight,
    surfaceVariant = themeColorsInUse.surfaceVariantLight,
    onSurfaceVariant = themeColorsInUse.onSurfaceVariantLight,
    outline = themeColorsInUse.outlineLight,
    outlineVariant = themeColorsInUse.outlineVariantLight,
    scrim = themeColorsInUse.scrimLight,
    inverseSurface = themeColorsInUse.inverseSurfaceLight,
    inverseOnSurface = themeColorsInUse.inverseOnSurfaceLight,
    inversePrimary = themeColorsInUse.inversePrimaryLight,
)

private val darkScheme = darkColorScheme(
    primary = themeColorsInUse.primaryDark,
    onPrimary = themeColorsInUse.onPrimaryDark,
    primaryContainer = themeColorsInUse.primaryContainerDark,
    onPrimaryContainer = themeColorsInUse.onPrimaryContainerDark,
    secondary = themeColorsInUse.secondaryDark,
    onSecondary = themeColorsInUse.onSecondaryDark,
    secondaryContainer = themeColorsInUse.secondaryContainerDark,
    onSecondaryContainer = themeColorsInUse.onSecondaryContainerDark,
    tertiary = themeColorsInUse.tertiaryDark,
    onTertiary = themeColorsInUse.onTertiaryDark,
    tertiaryContainer = themeColorsInUse.tertiaryContainerDark,
    onTertiaryContainer = themeColorsInUse.onTertiaryContainerDark,
    error = themeColorsInUse.errorDark,
    onError = themeColorsInUse.onErrorDark,
    errorContainer = themeColorsInUse.errorContainerDark,
    onErrorContainer = themeColorsInUse.onErrorContainerDark,
    background = themeColorsInUse.backgroundDark,
    onBackground = themeColorsInUse.onBackgroundDark,
    surface = themeColorsInUse.surfaceDark,
    onSurface = themeColorsInUse.onSurfaceDark,
    surfaceVariant = themeColorsInUse.surfaceVariantDark,
    onSurfaceVariant = themeColorsInUse.onSurfaceVariantDark,
    outline = themeColorsInUse.outlineDark,
    outlineVariant = themeColorsInUse.outlineVariantDark,
    scrim = themeColorsInUse.scrimDark,
    inverseSurface = themeColorsInUse.inverseSurfaceDark,
    inverseOnSurface = themeColorsInUse.inverseOnSurfaceDark,
    inversePrimary = themeColorsInUse.inversePrimaryDark,
)

@Composable
fun NutrifierTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    /*
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }*/

    val extraColors = if (darkTheme) {
        ExtraColors(
            success = successDark,
            onSuccess = onSuccessDark,
            successContainer = successContainerDark,
            onSuccessContainer = onSuccessContainerDark,
            warning = warningDark,
            onWarning = onWarningDark,
            warningContainer = warningContainerDark,
            onWarningContainer = onWarningContainerDark,
        )
    } else {
        ExtraColors(
            success = successLight,
            onSuccess = onSuccessLight,
            successContainer = successContainerLight,
            onSuccessContainer = onSuccessContainerLight,
            warning = warningLight,
            onWarning = onWarningLight,
            warningContainer = warningContainerLight,
            onWarningContainer = onWarningContainerLight,
        )
    }

    CompositionLocalProvider(LocalExtraColors provides extraColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}