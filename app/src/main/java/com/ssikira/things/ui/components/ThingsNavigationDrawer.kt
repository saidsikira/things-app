package com.ssikira.things.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.ssikira.things.R
import com.ssikira.things.data.Filter
import com.ssikira.things.data.Project
import com.ssikira.things.data.ThingsDatabase
import com.ssikira.things.data.ThingsRepository
import com.ssikira.things.viewmodel.ItemsViewModelFactory
import com.ssikira.things.viewmodel.Screen
import com.ssikira.things.viewmodel.ThingsViewModel
import com.ssikira.things.viewmodel.projectId
import com.ssikira.things.viewmodel.toFilter
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingsNavigationDrawer(
    vm: ThingsViewModel = viewModel(
        factory = ItemsViewModelFactory(
            ThingsRepository(ThingsDatabase.getInstance(LocalContext.current))
        )
    ),
    navController: NavHostController
) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route ?: "inbox"
    val projectId = currentDestination?.arguments?.getInt("id")
    val hintedFilter = getHintedFilter(currentRoute, projectId)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val projects by vm.projects.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                LazyColumn {
                    item {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "THINGS APP",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(listOf(Screen.Inbox, Screen.Today, Screen.Logbook)) {
                        DrawerItem(screen = it, currentRoute = currentRoute) {
                            navController.navigate(it)
                            scope.launch {
                                drawerState.apply { close() }
                            }
                        }
                    }

                    item {
                        HorizontalDivider(modifier = Modifier.padding(12.dp))
                        ProjectDrawerHeader {
                            val project = Project(title = "Personal")
                            vm.insertProject(project)
                        }
                    }

                    items(projects.map { Screen.Project(id = it.id, name = it.title) }) {
                        DrawerItem(
                            screen = it,
                            currentRoute = currentRoute,
                            currentProjectId = projectId
                        ) {
                            navController.navigate(it)
                            scope.launch {
                                drawerState.apply { close() }
                            }
                        }
                    }
                }
            }
        }) {
        Scaffold(
            topBar = {
                ThingsSearchBar(onDrawerAction = {
                    scope.launch {
                        drawerState.apply { if (isClosed) open() else close() }
                    }
                })
            },
            bottomBar = {
                ThingsBottomBar {
                    showBottomSheet = true
                }
            }
        ) { it ->
            ThingsNavigation(
                modifier = Modifier.padding(it),
                navController = navController
            )

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },

                    sheetState = sheetState
                ) {
                    NewTaskDialogContent(
                        hintedFilter = hintedFilter,
                        onTaskAdded = {
                            vm.insertItem(it)
                            showBottomSheet = false
                        })
                }
            }
        }
    }
}

fun getHintedFilter(route: String, projectId: Int?): Filter {
    return when {
        route.startsWith("today") -> Filter.Today()
        route.startsWith("inbox") -> Filter.Inbox

        route.startsWith("project/") -> {
            projectId?.let { Filter.Project(projectId) } ?: Filter.Inbox
        }

        else -> Filter.Inbox
    }
}

@Composable
fun ProjectDrawerHeader(
    onClick: () -> Unit
) {
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
        FilledTonalButton(onClick = {
            println("click");
            onClick()
        }) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun DrawerItem(
    screen: Screen,
    currentRoute: String?,
    currentProjectId: Int? = null,
    onClick: () -> Unit
) {
    val isSelected = screen.route == currentRoute && screen.projectId() == currentProjectId

    println("Screen: ${screen.route}, current route: $currentRoute")

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
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun getCurrentFilter(route: String, projects: List<Project>): Filter {
    return when {
        route.startsWith("today") -> Filter.Today()
        route.startsWith("project/") -> {
            val projectId = route.substringAfter("project/").toIntOrNull()
            projectId?.let { Filter.Project(it) } ?: Filter.Inbox
        }

        else -> Filter.Inbox
    }
}