package com.jh.presentation.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val ColorPalette = lightColors(
    primary = Color.White,
    primaryVariant = Color.White,
    secondary = Color.White,
    background = Color.White,
    surface = Color.White
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MurunTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalOverscrollConfiguration.provides(null)) {
        MaterialTheme(
            colors = ColorPalette,
            typography = Typography,
            content = content
        )
    }
}