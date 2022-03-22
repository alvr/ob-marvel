package dev.alvr.marvel.ui.base

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.alvr.marvel.ui.base.design.MarvelDarkTheme
import dev.alvr.marvel.ui.base.design.MarvelShapes
import dev.alvr.marvel.ui.base.design.MarvelTypography

@Composable
internal fun MarvelTheme(
    content: @Composable () -> Unit
) {
    val colors = MarvelDarkTheme
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(color = colors.surface, darkIcons = colors.isLight)
    }

    MaterialTheme(
        colors = colors,
        typography = MarvelTypography,
        shapes = MarvelShapes,
        content = content
    )
}
