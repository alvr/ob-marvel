package dev.alvr.marvel.navigation.screens

import androidx.navigation.NavType
import dev.alvr.marvel.ui.characters.CHARACTER_ARG

internal enum class NavArg(val key: String, val navType: NavType<*>) {
    Character(CHARACTER_ARG, NavType.IntType)
}
