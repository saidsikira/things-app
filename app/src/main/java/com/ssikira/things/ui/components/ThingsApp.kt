package com.ssikira.things.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ThingsApp() {
    val navController = rememberNavController()

    ThingsNavigationDrawer(navController = navController)
}