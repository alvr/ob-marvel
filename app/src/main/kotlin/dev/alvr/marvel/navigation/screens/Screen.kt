package dev.alvr.marvel.navigation.screens

import androidx.navigation.navArgument

internal sealed class Screen private constructor(
    internal val baseRoute: String,
    private val requiredArgs: List<NavArg> = emptyList(),
    private val optionalArgs: List<NavArg> = emptyList(),
) {
    object Home : Screen("home")
    object Details : Screen("details", requiredArgs = listOf(NavArg.Character)) {
        fun createRoute(id: Int) = "$baseRoute/$id"
    }

    val route = run {
        val required = requiredArgs.toRoute(separator = "/", prefix = "/") { "{${it.key}}" }
        val optional = optionalArgs.toRoute(separator = "&", prefix = "?") { "${it.key}={${it.key}}" }

        baseRoute + required + optional
    }

    val arguments = requiredArgs.map { arg ->
        navArgument(arg.key) { type = arg.navType }
    } + optionalArgs.map { arg ->
        navArgument(arg.key) {
            type = arg.navType
            nullable = true
        }
    }

    private fun List<NavArg>.toRoute(
        separator: String,
        prefix: String,
        transform: (NavArg) -> CharSequence
    ) = takeIf { isNotEmpty() }
        ?.joinToString(separator = separator, prefix = prefix, transform = transform)
        ?: NO_ARGUMENT

    companion object {
        private const val NO_ARGUMENT = ""
    }
}
