package com.ssikira.things.ui.components

import android.widget.SearchView
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ssikira.things.R
import com.ssikira.things.data.Item
import com.ssikira.things.data.ThingsDatabase
import com.ssikira.things.viewmodel.ItemsViewModelFactory
import com.ssikira.things.viewmodel.Screen
import com.ssikira.things.viewmodel.ThingsViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingsNavigation(
    vm: ThingsViewModel = viewModel(
        factory = ItemsViewModelFactory(
            ThingsDatabase.getInstance(
                LocalContext.current
            )
        )
    ),
    navController: NavHostController
) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route ?: "inbox"

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "THINGS APP",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                listOf(Screen.Inbox, Screen.Today, Screen.Logbook).forEach {
                    DrawerItem(screen = it, currentRoute = currentRoute) {
                        navController.navigate(it)
                        scope.launch {
                            drawerState.apply { close() }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Projects",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    FilledTonalButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }

                }

                // Projects list from the view model

            }
        }) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ThingsTopBar(
                    screen = Screen.forRoute(currentRoute),
                    scrollBehavior = scrollBehavior
                ) {
                    scope.launch {
                        drawerState.apply { if (isClosed) open() else close() }
                    }
                }
            },
            bottomBar = {
                ThingsBottomBar {
                    showBottomSheet = true
                }
            }
        ) { it ->
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

                composable(Screen.Logbook.route) {
                    ThingsList()
                }


            }
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState
                ) {
                    TaskEntry(onTaskAdded = {
                        vm.insertItem(it)
                        showBottomSheet = false
                    })
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
fun RouteIcon(route: String, modifier: Modifier = Modifier) {
    when (route) {
        "today" -> Icon(Icons.Filled.Star, contentDescription = "Today", modifier = modifier)
        "log" -> Icon(
            modifier = modifier,
            painter = rememberVectorPainter(
                image = ImageVector.vectorResource(
                    id = R.drawable.library_books
                )
            ),
            contentDescription = "Inventory"
        )

        "inbox" -> Icon(
            modifier = modifier,
            painter = rememberVectorPainter(
                image = ImageVector.vectorResource(
                    id = R.drawable.inventory
                )
            ),
            contentDescription = "Inventory"
        )
    }
}

fun NavHostController.navigate(screen: Screen) {
    this.navigate(screen.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}