package com.ssikira.things.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ssikira.things.data.Filter
import com.ssikira.things.data.ThingsDatabase
import com.ssikira.things.data.ThingsRepository
import com.ssikira.things.viewmodel.ItemsViewModelFactory
import com.ssikira.things.viewmodel.Screen
import com.ssikira.things.viewmodel.ThingsViewModel
import com.ssikira.things.viewmodel.toFilter

@Composable
fun ThingsNavigation(
    modifier: Modifier,
    navController: NavHostController
) {
    val viewModel: ThingsViewModel = viewModel(
        factory = ItemsViewModelFactory(
            ThingsRepository(ThingsDatabase.getInstance(LocalContext.current))
        )
    )

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "inbox"
    ) {
        composable(Screen.Inbox.route) {
            ThingsList(filter = Screen.Inbox.toFilter(), vm = viewModel)
        }

        composable(Screen.Today.route) {
            ThingsList(filter = Screen.Today.toFilter(), vm = viewModel)
        }

        composable(Screen.Logbook.route) {
            ThingsList(filter = Screen.Logbook.toFilter(), vm = viewModel)
        }

        composable(
            route = "project/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { entry ->
            val id = entry.arguments?.getInt("id")
            println("navigation new id: ${id}")
            ThingsList(filter = Filter.Project(id!!), vm = viewModel)
        }
    }
}