package dev.alvr.marvel.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dev.alvr.marvel.navigation.screens.Screen
import dev.alvr.marvel.navigation.screens.ScreensNavigator

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun MarvelNavigator() {
    val navController = rememberAnimatedNavController()
    val navigator = remember(navController) { ScreensNavigator(navController) }

    Scaffold(content = { paddingValues ->
        AnimatedNavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            home(navigator)
            details()
        }
    })
}
