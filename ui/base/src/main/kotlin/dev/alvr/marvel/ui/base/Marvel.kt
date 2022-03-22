package dev.alvr.marvel.ui.base

import androidx.compose.runtime.Composable

@Composable
fun Marvel(content: @Composable () -> Unit) {
    MarvelTheme(content)
}
