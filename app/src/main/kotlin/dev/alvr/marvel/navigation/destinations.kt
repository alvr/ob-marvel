package dev.alvr.marvel.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import dev.alvr.marvel.navigation.screens.Screen
import dev.alvr.marvel.navigation.screens.ScreensNavigator
import dev.alvr.marvel.ui.characters.details.CharacterDetails
import dev.alvr.marvel.ui.characters.list.Characters

@ExperimentalAnimationApi
internal fun NavGraphBuilder.home(navigator: ScreensNavigator) {
    composable(
        route = Screen.Home.route,
        enterTransition = homeEnterTransition,
        exitTransition = homeExitTransition,
    ) {
        Characters(navigator)
    }
}

@ExperimentalAnimationApi
internal fun NavGraphBuilder.details() {
    composable(
        route = Screen.Details.route,
        arguments = Screen.Details.arguments,
        enterTransition = homeEnterTransition,
        exitTransition = homeExitTransition,
    ) {
        CharacterDetails()
    }
}
