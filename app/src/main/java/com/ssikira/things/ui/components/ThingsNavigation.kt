package com.ssikira.things.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ssikira.things.viewmodel.Screen

@Composable
fun ThingsNavigation(
    modifier: Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "inbox"
    ) {
        composable(Screen.Inbox.route) {
            ThingsList()
        }

        composable(Screen.Today.route) {
            ThingsList()
        }

        composable(Screen.Logbook.route) {
            ThingsList()
        }

        composable(
            route = "project/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("id")
            ThingsList()
        }
    }
}