package com.example.mafia.navigation

sealed class NavigationRoutes(
    val route: String
) {
    object Welcome: NavigationRoutes("welcome_screen")
    object Start: NavigationRoutes("start_screen")
    object Lobby: NavigationRoutes("lobby_screen")
    object Loading: NavigationRoutes("loading_screen")
    object Day: NavigationRoutes("day_screen")
    object Voting: NavigationRoutes("voting_screen")
    object Death: NavigationRoutes("death_screen")
    object Arrest: NavigationRoutes("arrest_screen")

    object Win: NavigationRoutes("win_screen")
}
