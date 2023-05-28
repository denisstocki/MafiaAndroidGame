package com.example.mafia.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mafia.composes.*

@Composable
fun Navigation(context: Context)
{
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Welcome.route
    ){

        composable(route = NavigationRoutes.Welcome.route) {
            WelcomeAnimation(navController = navController)
        }

        composable(route = NavigationRoutes.Start.route) {
            StartCompose(navController = navController)
        }

        composable(route = NavigationRoutes.Lobby.route) {
            LobbyCompose(navController = navController)
        }

        composable(route = NavigationRoutes.Loading.route) {
            LoadingCompose(navController = navController)
        }

        composable(route = NavigationRoutes.Day.route) {
            DayCompose(navController = navController)
        }

        composable(route = NavigationRoutes.Voting.route) {
            VotingCompose(navController = navController)
        }

        composable(route = NavigationRoutes.Death.route) {
            DeathCompose(navController = navController)
        }

        composable(route = NavigationRoutes.Win.route) {
            WinCompose(navController = navController)
        }
    }
}