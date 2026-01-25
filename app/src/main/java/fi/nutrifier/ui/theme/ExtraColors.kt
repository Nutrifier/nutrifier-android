package fi.nutrifier.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val successLight = Color(0xFF349439)
val onSuccessLight = Color(0xFFFFFFFF)
val successContainerLight = Color(0xFFBCEEBD)
val onSuccessContainerLight = Color(0xFF1B5E20)

val warningLight = Color(0xFFFF9E00)
val onWarningLight = Color(0xFF000000)
val warningContainerLight = Color(0xFFF8E1A5)
val onWarningContainerLight = Color(0xFF5F3D00)

val successDark = Color(0xFF81C784)
val onSuccessDark = Color(0xFF1B5E20)
val successContainerDark = Color(0xFF4A9450)
val onSuccessContainerDark = Color(0xFFEDFFED)

val warningDark = Color(0xFFF3A541)
val onWarningDark = Color(0xFF000000)
val warningContainerDark = Color(0xFFFCD485)
val onWarningContainerDark = Color(0xFF5F3D00)

data class ExtraColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
)

val LocalExtraColors = staticCompositionLocalOf {
    ExtraColors(
        success = Color.Green,
        onSuccess = Color.White,
        successContainer = Color.Green,
        onSuccessContainer = Color.White,
        warning = Color.Yellow,
        onWarning = Color.Black,
        warningContainer = Color.Yellow,
        onWarningContainer = Color.Black,
    )
}