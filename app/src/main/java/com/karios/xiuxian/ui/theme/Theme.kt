package com.karios.xiuxian.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val InkBlack = Color(0xFF0B0E0C)
val InkSurface = Color(0xFF141A15)
val InkSurface2 = Color(0xFF1D251D)
val InkLine = Color(0xFF2A332A)
val Gold = Color(0xFFD4AF37)
val GoldLight = Color(0xFFF0D97A)
val JadeGreen = Color(0xFF7FB685)
val BloodRed = Color(0xFFC05A4E)
val PaperText = Color(0xFFE6E0CE)
val MutedText = Color(0xFF8C8C7A)

private val XiuColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = InkBlack,
    secondary = JadeGreen,
    onSecondary = InkBlack,
    background = InkBlack,
    onBackground = PaperText,
    surface = InkSurface,
    onSurface = PaperText,
    surfaceVariant = InkSurface2,
    onSurfaceVariant = MutedText,
    error = BloodRed,
    onError = InkBlack
)

val SerifFont = FontFamily.Serif

private val XiuTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = SerifFont, fontSize = 16.sp, color = PaperText, lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = SerifFont, fontSize = 14.sp, color = PaperText, lineHeight = 21.sp
    ),
    titleLarge = TextStyle(
        fontFamily = SerifFont, fontSize = 22.sp, color = Gold,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = TextStyle(
        fontFamily = SerifFont, fontSize = 17.sp, color = Gold,
        fontWeight = FontWeight.Bold
    ),
    labelSmall = TextStyle(
        fontFamily = SerifFont, fontSize = 11.sp, color = MutedText
    ),
    labelMedium = TextStyle(
        fontFamily = SerifFont, fontSize = 13.sp, color = MutedText
    )
)

@Composable
fun XiuXianTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = XiuColorScheme,
        typography = XiuTypography,
        content = content
    )
}