package com.example.mafia.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mafia.composes.*
import com.example.mafia.viewmodel.GameViewModel

@Composable
fun Navigation(context: Context, gameViewModel: GameViewModel)
{
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Welcome.route
    ){

        // Works fine
        composable(route = NavigationRoutes.Welcome.route) {
            WelcomeAnimation(navController = navController)
        }

        // Works fine
        composable(route = NavigationRoutes.Start.route) {
            StartAnimation(navController = navController,gameViewModel)
        }

        composable(route = NavigationRoutes.Lobby.route) {
            LobbyCompose(navController = navController,gameViewModel)
        }

        composable(route = NavigationRoutes.Loading.route) {
            LoadingCompose(navController = navController,gameViewModel)
        }

        composable(route = NavigationRoutes.Day.route) {
            DayCompose(navController = navController, gameViewModel)
        }

        composable(route = NavigationRoutes.Voting.route) {
            VotingCompose(navController = navController, gameViewModel)
        }

        composable(route = NavigationRoutes.Death.route) {
            DeathCompose(navController = navController)
        }

        composable(route = NavigationRoutes.Win.route) {
            WinCompose(navController = navController)
        }
    }
}