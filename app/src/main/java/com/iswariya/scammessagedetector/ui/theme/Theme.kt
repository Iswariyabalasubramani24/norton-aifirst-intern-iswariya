package com.iswariya.scammessagedetector.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


val NortonYellow = Color(0xFFFFD100)   // Signature golden yellow
val NortonOnYellow = Color(0xFF1A1600)   // Dark text/icons on yellow

// ── Dark navy surfaces ────────────────────────────────────────────────────────
val NortonNavy = Color(0xFF0A0D14)    // Main background
val NortonCard = Color(0xFF141921)    // Cards / surfaces
val NortonInput = Color(0xFF1C2230)   // Inputs / surfaceVariant
val NortonDimText = Color(0xFF8899B0)   // Muted text

// ── Risk indicator colours ────────────────────────────────────────────────────
val DangerRed = Color(0xFFE24B4A)
val DangerBg = Color(0xFFFCEBEB)
val DangerBorder = Color(0xFFF09595)

val WarnAmber = Color(0xFFCC8800)
val WarnBg = Color(0xFFFAEEDA)
val WarnBorder = Color(0xFFFAC775)

val SafeGreen = Color(0xFF3B7A12)
val SafeBg = Color(0xFFEAF3DE)
val SafeBorder = Color(0xFFC0DD97)

// ── Colour schemes ────────────────────────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary = NortonYellow,
    onPrimary = NortonOnYellow,
    background = NortonNavy,
    surface = NortonCard,
    surfaceVariant = NortonInput,
    onBackground = Color(0xFFF0F2F5),
    onSurface = Color(0xFFE8EAF0),
    onSurfaceVariant = NortonDimText,
    outline = Color(0xFF252D3D),
    error = DangerRed
)

private val LightColorScheme = lightColorScheme(
    primary = NortonYellow,
    onPrimary = NortonOnYellow,
    background = Color(0xFFF4F5F8),
    surface = Color.White,
    surfaceVariant = Color(0xFFEEEFF2),
    onBackground = Color(0xFF0D1117),
    onSurface = Color(0xFF111318),
    onSurfaceVariant = Color(0xFF555C6B),
    outline = Color(0xFFD5D7DE),
    error = DangerRed
)

// ── Typography ────────────────────────────────────────────────────────────────
val AppTypography = Typography(
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = (-0.2).sp
    ),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 19.sp),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 16.sp),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        letterSpacing = 0.3.sp
    ),
    labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.8.sp)
)

// ── Theme entry point ─────────────────────────────────────────────────────────
@Composable
fun ScamMessageDetectorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = AppTypography,
        content = content
    )
}
