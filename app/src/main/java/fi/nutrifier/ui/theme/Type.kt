package fi.nutrifier.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    //displayLarge = TextStyle(),
    //displayMedium = TextStyle(),
    //displaySmall = TextStyle(),
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineMedium = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold
    ),
    headlineSmall = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    ),
    //titleLarge = TextStyle(),
    titleMedium = TextStyle(
        fontSize = 18.sp
    ),
    //titleSmall = TextStyle(),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    //bodyMedium = TextStyle(),
    //bodySmall = TextStyle(),
    labelLarge = TextStyle(
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp
    ),
)