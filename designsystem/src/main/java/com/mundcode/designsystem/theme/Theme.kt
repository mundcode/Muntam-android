package com.mundcode.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

val DarkColorPalette = darkColors(
    background = Gray900,
    onBackground = White
)

val LightColorPalette = lightColors(
    background = White,
    onBackground = Gray900
)
