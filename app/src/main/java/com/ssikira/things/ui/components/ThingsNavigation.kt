package com.ssikira.things.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ssikira.things.R
import com.ssikira.things.viewmodel.Screen
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingsNavigation(
    navController: NavHostController
) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Things",
                    style = MaterialTheme.typography.headlineLarge
                )
                DrawerItem(screen = Screen.Inbox, currentRoute = currentRoute) {
                    navController.navigate(Screen.Inbox.route)
                    scope.launch {
                        drawerState.apply { close() }
                    }
                }

                DrawerItem(screen = Screen.Today, currentRoute = currentRoute) {
                    navController.navigate(Screen.Today.route)
                    scope.launch {
                        drawerState.apply { close() }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(16.dp))
            }
        }) {
        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = { Text(text = "Today") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.apply { if (isClosed) open() else close() }
                                }
                            }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    actions = {},
                    floatingActionButton = {
                        FloatingActionButton(
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                            onClick = { /*TODO*/ }) {
                            Icon(Icons.Filled.Add, contentDescription = "Add Item")
                        }
                    }
                )
            }
        ) {
            NavHost(
                modifier = Modifier.padding(it),
                navController = navController,
                startDestination = "inbox"
            ) {
                composable(Screen.Inbox.route) {
                    ThingsList()
                }

                composable(Screen.Today.route) {
                    ThingsList()
                }
            }
        }
    }
}

@Composable
fun DrawerItem(screen: Screen, currentRoute: String?, onClick: () -> Unit) {
    val isSelected = screen.route == currentRoute

    NavigationDrawerItem(
        modifier = Modifier.padding(horizontal = 16.dp),
        label = { Text(text = screen.title) },
        icon = { RouteIcon(route = screen.route) },
        selected = isSelected,
        onClick = onClick
    )
}

@Composable
fun RouteIcon(route: String) {
    when (route) {
        "inbox" -> Icon(Icons.Filled.Star, contentDescription = "Today")
        "today" -> Icon(
            painter = rememberVectorPainter(
                image = ImageVector.vectorResource(
                    id = R.drawable.inventory
                )
            ),
            contentDescription = "Inventory"
        )
    }
}