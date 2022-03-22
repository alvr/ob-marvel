package dev.alvr.marvel.navigation.screens

import androidx.navigation.NavController
import dev.alvr.marvel.ui.characters.CharactersNavigator

internal class ScreensNavigator(private val navController: NavController) : CharactersNavigator {
    override fun goToDetails(id: Int?) {
        id?.let {
            navController.navigate(Screen.Details.createRoute(id))
        }
    }
}
